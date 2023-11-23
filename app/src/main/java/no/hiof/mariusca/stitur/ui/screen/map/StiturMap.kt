package no.hiof.mariusca.stitur.ui.screen.map

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.mariusca.stitur.model.Coordinate
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.ui.screen.map.dialogs.SaveGeoTreasureDialog
import no.hiof.mariusca.stitur.ui.screen.map.dialogs.SaveTripDialog
import no.hiof.mariusca.stitur.ui.screen.map.dialogs.ShowGeoTreasureDialog
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StiturMap(
    viewModel: TripViewModel = hiltViewModel(),
    treasureViewModel: GeoTreasureViewModel = hiltViewModel(),
    isCreateTripMode: MutableState<Boolean>,
    newTripPoints: MutableList<LatLng>,
    selectedTripState: MutableState<Trip?>,
    selectedTreasureState: MutableState<GeoTreasure?>,
    newTrip: MutableState<Trip?>,
    openSaveTripDialog: MutableState<Boolean>,
    ongoingTripState: MutableState<Trip?>,
    isLoading: MutableState<Boolean>,
    cameraFollowingGps: MutableState<Boolean>,
    openSaveGeoTreasureDialog: MutableState<Boolean>,
    newGeoTreasure: MutableState<GeoTreasure?>,
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val treasures by treasureViewModel.treasures.collectAsStateWithLifecycle(emptyList())
    val trips by viewModel.trips.collectAsStateWithLifecycle(emptyList())

    val halden = LatLng(59.1330, 11.3875)
    val cameraPosition = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(halden, 13f)
    }

    val context = LocalContext.current

    val toggleBottomSheet: (Boolean) -> Unit = { showBottomSheet = it }

    val newTripHistoryState = remember { mutableStateOf<TripHistory?>(null) }
    val gpsTripState = remember { mutableStateOf<Trip?>(null) }
    val locationRequest = remember {
        mutableStateOf<LocationRequest?>(null)
    }
    val latestCoordinate = remember { mutableStateOf<LatLng?>(null) }


    LaunchedEffect(selectedTripState.value) {
        if (selectedTripState.value != null) {
            val startCoordinate = selectedTripState.value!!.coordinates[0]
            val startLatLng =
                LatLng(startCoordinate.lat.toDouble(), startCoordinate.long.toDouble())

            cameraPosition.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                        startLatLng,
                        14f
                    )
                )
            )
            sheetState.show()
            showBottomSheet = true
        }
    }

    CameraFollowGps(cameraFollowingGps, gpsTripState, cameraPosition)

    MapContent(
        trips = trips,
        selectedTripState = selectedTripState,
        ongoingTripState = ongoingTripState,
        cameraPosition = cameraPosition,
        newTripPoints = newTripPoints,
        context = context,
        isCreateTripMode = isCreateTripMode,
        gpsTripState = gpsTripState,
        treasure = treasures,
        selectedTreasureState = selectedTreasureState,
        isLoading = isLoading
    )

    ShowDialogs(
        openSaveTripDialog,
        newTrip,
        viewModel,
        newTripPoints,
        isCreateTripMode,
        openSaveGeoTreasureDialog,
        locationRequest,
        newGeoTreasure,
        treasureViewModel,
        latestCoordinate,
        selectedTreasureState
    )

    MapBottomSheet(
        selectedTripState = selectedTripState,
        sheetState = sheetState,
        showBottomSheet = showBottomSheet,
        toggleBottomSheet = toggleBottomSheet,
        scope = scope,
        ongoingTripState = ongoingTripState,
        viewModel = viewModel,
        newTripHistoryState = newTripHistoryState,
        gpsTripState = gpsTripState,
        locationRequest = locationRequest,
    )

    LocationRequestHandler(locationRequest, latestCoordinate, gpsTripState)
}

@Composable
private fun CameraFollowGps(
    cameraFollowingGps: MutableState<Boolean>,
    gpsTripState: MutableState<Trip?>,
    cameraPosition: CameraPositionState
) {
    if (cameraFollowingGps.value) {
        LaunchedEffect(gpsTripState.value?.coordinates) {
            if (gpsTripState.value?.coordinates?.isNotEmpty() == true) {
                val lastCoordinate = gpsTripState.value!!.coordinates.last()
                val startLatLng =
                    LatLng(lastCoordinate.lat.toDouble(), lastCoordinate.long.toDouble())

                cameraPosition.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            startLatLng,
                            14f
                        )
                    )
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun LocationRequestHandler(
    locationRequest: MutableState<LocationRequest?>,
    latestCoordinate: MutableState<LatLng?>,
    gpsTripState: MutableState<Trip?>
) {
    if (locationRequest.value != null) {
        LocationUpdatesEffect(locationRequest.value!!) { result ->
            latestCoordinate.value =
                result.lastLocation?.let { LatLng(it.latitude, it.longitude) }
            for (currentLocation in result.locations) {
                val newCoordinate = Coordinate(
                    lat = currentLocation.latitude.toString(),
                    long = currentLocation.longitude.toString()
                )

                // Checks if the new coordinate is not already in the list
                if (!gpsTripState.value?.coordinates.orEmpty().contains(newCoordinate)) {
                    val updatedCoordinates =
                        (gpsTripState.value?.coordinates ?: emptyList()) + newCoordinate
                    gpsTripState.value =
                        gpsTripState.value?.copy(coordinates = updatedCoordinates)
                }
            }
        }
    }
}

@Composable
private fun ShowDialogs(
    openSaveTripDialog: MutableState<Boolean>,
    newTrip: MutableState<Trip?>,
    viewModel: TripViewModel,
    newTripPoints: MutableList<LatLng>,
    isCreateTripMode: MutableState<Boolean>,
    openSaveGeoTreasureDialog: MutableState<Boolean>,
    locationRequest: MutableState<LocationRequest?>,
    newGeoTreasure: MutableState<GeoTreasure?>,
    treasureViewModel: GeoTreasureViewModel,
    latestCoordinate: MutableState<LatLng?>,
    selectedTreasureState: MutableState<GeoTreasure?>
) {
    when {
        openSaveTripDialog.value -> {
            SaveTripDialog(
                openSaveTripDialog,
                newTrip,
                viewModel,
                newTripPoints,
                isCreateTripMode
            )
        }
    }

    when {
        openSaveGeoTreasureDialog.value -> {
            if (locationRequest.value == null) {
                locationRequest.value = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(60)
                ).build()
            }
            SaveGeoTreasureDialog(
                openSaveGeoTreasureDialog,
                newGeoTreasure,
                treasureViewModel,
                latestCoordinate,
                locationRequest,
            )
        }
    }

    when {
        selectedTreasureState.value != null -> {
            ShowGeoTreasureDialog(
                selectedTreasureState,
                treasureViewModel,
            )
        }
    }
}