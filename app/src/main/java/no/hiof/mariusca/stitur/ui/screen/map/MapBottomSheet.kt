package no.hiof.mariusca.stitur.ui.screen.map

import android.Manifest
import android.annotation.SuppressLint
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.ui.screen.tripHistory.TripHistoryViewModel
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapBottomSheet(
    tripHistoryViewModel: TripHistoryViewModel = hiltViewModel(),
    selectedTripState: MutableState<Trip?>,
    sheetState: SheetState,
    showBottomSheet: Boolean,
    toggleBottomSheet: (Boolean) -> Unit,
    scope: CoroutineScope,
    ongoingTripState: MutableState<Trip?>,
    viewModel: StiturMapViewModel,
    newTripHistoryState: MutableState<TripHistory?>,
    gpsTripState: MutableState<Trip?>,
    locationRequest: MutableState<LocationRequest?>
) {

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                toggleBottomSheet(false)
                selectedTripState.value = null
            }, sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(10.dp, top = 0.dp, bottom = 16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                ) {
                    Button(modifier = Modifier.padding(end = 10.dp), onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                toggleBottomSheet(false)
                                selectedTripState.value = null
                            }
                        }
                    }) {
                        Text("Close (X)")
                    }
                }

                DynamicStartAndStopButton(
                    tripHistoryViewModel,
                    selectedTripState,
                    ongoingTripState,
                    newTripHistoryState,
                    scope,
                    sheetState,
                    toggleBottomSheet,
                    gpsTripState,
                    locationRequest
                )

                TripOverview(selectedTripState)

                Button(modifier = Modifier.padding(top = 14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    onClick = {
                        selectedTripState.value?.let { viewModel.deleteTrip(it) }
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                toggleBottomSheet(false)
                                selectedTripState.value = null
                            }
                        }
                    }) {
                    Text("Delete trip")
                }
            }

        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DynamicStartAndStopButton(
    viewModel: TripHistoryViewModel,
    selectedTripState: MutableState<Trip?>,
    ongoingTripState: MutableState<Trip?>,
    newTripHistoryState: MutableState<TripHistory?>,
    scope: CoroutineScope,
    sheetState: SheetState,
    toggleBottomSheet: (Boolean) -> Unit,
    gpsTripState: MutableState<Trip?>,
    locationRequest: MutableState<LocationRequest?>
) {


    if (selectedTripState.value == ongoingTripState.value) {
        Button(modifier = Modifier.padding(bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006600)),
            onClick = {
                locationRequest.value = null

                val tripStartDate = newTripHistoryState.value?.date?.toInstant()
                if (tripStartDate != null) {
                    val currentInstant = Instant.now()
                    val duration = Duration.between(tripStartDate, currentInstant)
                    val minutes = duration.toMinutes()

                    // Round properly by adding 0.5 and converting to integer
                    val roundedMinutes = (minutes + 0.5).toInt()
                    newTripHistoryState.value?.durationMinutes = roundedMinutes
                }
                newTripHistoryState.value?.trackedDistanceKm = 1.2
                newTripHistoryState.value?.let { viewModel.createTripHistory(it) }
                newTripHistoryState.value = null
                ongoingTripState.value = null
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        toggleBottomSheet(false)
                        selectedTripState.value = null
                    }
                }
            }) {
            Text("End trip")
        }
    } else {
        Button(modifier = Modifier.padding(bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006600)),
            onClick = {
                gpsTripState.value = Trip()
                locationRequest.value = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    TimeUnit.SECONDS.toMillis(5)
                ).build()
                ongoingTripState.value = selectedTripState.value
                newTripHistoryState.value =
                    ongoingTripState.value?.let { TripHistory(trip = it, pointsEarned = 100) }
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        toggleBottomSheet(false)
                        selectedTripState.value = null
                    }
                }
            }) {
            Text("Start trip")
        }
    }
}

@Composable
private fun TripOverview(selectedTripState: MutableState<Trip?>) {
    selectedTripState.value?.let { selectedTrip ->
        Text("Selected Trip: ${selectedTrip.routeName}")
        Text("Description: ${selectedTrip.routeDescription}")
        Text("Difficulty: ${selectedTrip.difficulty}")
    }
}

@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun LocationUpdatesEffect(
    locationRequest: LocationRequest,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onUpdate: (result: LocationResult) -> Unit,
) {
    val context = LocalContext.current
    val currentOnUpdate by rememberUpdatedState(newValue = onUpdate)

    // Whenever on of these parameters changes, dispose and restart the effect.
    DisposableEffect(locationRequest, lifecycleOwner) {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                currentOnUpdate(result)
            }
        }
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                locationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper(),
                )
            } else if (event == Lifecycle.Event.ON_STOP) {
                locationClient.removeLocationUpdates(locationCallback)
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
