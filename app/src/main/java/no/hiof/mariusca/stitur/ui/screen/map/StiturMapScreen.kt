package no.hiof.mariusca.stitur.ui.screen.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.Coordinate
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.ui.screen.GeoTreasureViewModel
import no.hiof.mariusca.stitur.ui.screen.map.dialogs.SaveGeoTreasureDialog
import no.hiof.mariusca.stitur.ui.screen.map.dialogs.SaveTripDialog
import no.hiof.mariusca.stitur.ui.screen.map.dialogs.ShowGeoTreasureDialog
import java.util.concurrent.TimeUnit

@Composable
fun ColumnItem(item: String, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .padding(10.dp)
    ) {
        Text(text = item, modifier = Modifier.padding(vertical = 10.dp), fontSize = 22.sp)
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>, placeHolder: String, modifier: Modifier
) {
    TextField(value = state.value, onValueChange = { value ->
        state.value = value
    },

        modifier
            .fillMaxWidth()
            .padding(80.dp, 50.dp, 80.dp, 0.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp))
            .height(50.dp), leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon"
            )
        }, placeholder = {
            Text(text = placeHolder)
        }, colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
        ), maxLines = 1, singleLine = true
    )
}


@Composable
fun StiturMapScreen(
    weatherIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StiturMapViewModel = hiltViewModel(),
) {
    val filteredTrips = viewModel.filteredTrips
    val isCreateTripMode = remember { mutableStateOf(false) }
    val newTripPoints = remember {
        mutableStateListOf<LatLng>()
    }
    val selectedTripState = remember { mutableStateOf<Trip?>(null) }
    val selectedTreasureState = remember { mutableStateOf<GeoTreasure?>(null) }

    val newTrip = remember { mutableStateOf<Trip?>(null) }
    val newGeoTreasure = remember { mutableStateOf<GeoTreasure?>(null) }

    val ongoingTripState = remember { mutableStateOf<Trip?>(null) }

    val openSaveTripDialog = remember { mutableStateOf(false) }

    val openSaveGeoTreasureDialog = remember { mutableStateOf(false) }


    // Inspired by https://github.com/android/platform-samples/blob/main/samples/base/src/main/java/com/example/platform/base/PermissionBox.kt
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val cameraFollowingGps = remember { mutableStateOf(false) }

    // Does not work well. Just white for the most part when loading. Not sure why.
    val isLoading = remember { mutableStateOf(true) }
    if (isLoading.value) {
        Text(text = "Loading..", fontSize = 22.sp)
    }
    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first()),
        onGranted = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                StiturMap(
                    isCreateTripMode = isCreateTripMode,
                    newTripPoints = newTripPoints,
                    selectedTripState = selectedTripState,
                    selectedTreasureState = selectedTreasureState,
                    newTrip = newTrip,
                    openSaveTripDialog = openSaveTripDialog,
                    ongoingTripState = ongoingTripState,
                    isLoading = isLoading,
                    cameraFollowingGps = cameraFollowingGps,
                    openSaveGeoTreasureDialog = openSaveGeoTreasureDialog,
                    newGeoTreasure = newGeoTreasure
                )

                IconButton(onClick = weatherIconClicked) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_weathericon),
                        contentDescription = "Weather icon",
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopStart)
                    )
                }
                MapButtons(
                    isCreateTripMode = isCreateTripMode,
                    newTripPoints = newTripPoints,
                    newTrip = newTrip,
                    openNewTripDialog = openSaveTripDialog,
                    ongoingTripState = ongoingTripState,
                    selectedTripState = selectedTripState,
                    cameraFollowingGps = cameraFollowingGps,
                    openNewGeoTreasureDialog = openSaveGeoTreasureDialog,
                    newGeoTreasure = newGeoTreasure
                )
            }
            Column(modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally) {

                val textState = remember {
                    mutableStateOf(TextFieldValue(""))
                }
                SearchView(
                    state = textState,
                    placeHolder = "Search for trailwalks!",
                    modifier = modifier
                )

                val searchedText = textState.value.text.lowercase()

                if (searchedText.isNotBlank()) {

                    viewModel.getCreatedTrip(searchedText)
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        items(items = filteredTrips, key = { it.uid }) { item ->
                            ColumnItem(item = item.routeName, onItemClick = {
                                selectedTripState.value = item
                            })
                        }
                    }
                }
                var showInstruction by remember { mutableStateOf(false) }


                LaunchedEffect(isCreateTripMode.value) {
                    if (isCreateTripMode.value) {
                        showInstruction = true
                        delay(3000)
                        showInstruction = false
                    } else {
                        showInstruction = false
                    }
                }


                AnimatedVisibility(
                    visible = showInstruction,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Tap to draw your route!",
                                fontSize = 28.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.tap),
                            contentDescription = "Tap instruction",
                            modifier = Modifier
                                .size(180.dp)
                                .padding(top = 20.dp, end = 70.dp)
                                .align(Alignment.End)
                        )
                    }
                }
            }
        })
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StiturMap(
    viewModel: StiturMapViewModel = hiltViewModel(),
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

    Column {
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
}


