package no.hiof.mariusca.stitur.ui.screen.map

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.ui.screen.map.search.SearchResult
import no.hiof.mariusca.stitur.ui.screen.map.search.TripsSearchBar
import no.hiof.mariusca.stitur.ui.screen.map.utils.PermissionBox

@Composable
fun StiturMapScreen(
    weatherIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TripViewModel = hiltViewModel(),
) {

    var isSearchActive by remember { mutableStateOf(false) }

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

    // Does not work well. Just white screen for the most part when loading. Not in use for now.
    val isLoading = remember { mutableStateOf(true) }

    var showInstruction by remember { mutableStateOf(false) }

    val textState = remember {
        mutableStateOf(TextFieldValue(""))
    }


    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first())
    ) {
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


            TripsSearchBar(
                state = textState,
                placeHolder = "Search for trailwalks!",
                modifier = modifier
            )

            isSearchActive =
                SearchResult(textState, isSearchActive, viewModel, filteredTrips, selectedTripState)


            LaunchedEffect(textState.value) {
                isSearchActive = textState.value.text.isNotBlank()

            }

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
    }
}


