package no.hiof.mariusca.stitur.ui.screen.map

import android.annotation.SuppressLint
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.LeaderboardEntry
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.model.calculateDistanceKM
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.ProfileViewModel
import no.hiof.mariusca.stitur.ui.screen.leaderboard.StiturLeaderboardsViewModel
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapBottomSheet(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    leaderboardsViewModel: StiturLeaderboardsViewModel = hiltViewModel(),
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
                    leaderboardsViewModel,
                    profileViewModel,
                    signUpViewModel,
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
    leaderboardsViewModel: StiturLeaderboardsViewModel,
    profileViewModel: ProfileViewModel,
    signUpViewModel: SignUpViewModel,
    selectedTripState: MutableState<Trip?>,
    ongoingTripState: MutableState<Trip?>,
    newTripHistoryState: MutableState<TripHistory?>,
    scope: CoroutineScope,
    sheetState: SheetState,
    toggleBottomSheet: (Boolean) -> Unit,
    gpsTripState: MutableState<Trip?>,
    locationRequest: MutableState<LocationRequest?>
) {
    profileViewModel.getUserInfo(signUpViewModel.currentLoggedInUserId)
    val loggedInProfile = profileViewModel.filteredUser

    leaderboardsViewModel.getUserInfo(signUpViewModel.currentLoggedInUserId)
    val loggedInLeaderboardEntry = leaderboardsViewModel.filteredUser

    if (selectedTripState.value == ongoingTripState.value) {
        Button(modifier = Modifier.padding(bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006600)),
            onClick = {
                finishTrip(
                    leaderboardsViewModel,
                    loggedInLeaderboardEntry,
                    locationRequest,
                    newTripHistoryState,
                    gpsTripState,
                    loggedInProfile,
                    profileViewModel,
                    ongoingTripState,
                    scope,
                    sheetState,
                    toggleBottomSheet,
                    selectedTripState
                )
            }) {
            Text("End trip")
        }
    } else {
        Button(modifier = Modifier.padding(bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006600)),
            onClick = {
                gpsTripState.value = Trip()
                locationRequest.value = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(5)
                ).build()
                ongoingTripState.value = selectedTripState.value
                newTripHistoryState.value =
                    ongoingTripState.value?.let { TripHistory(tripId = it.uid, pointsEarned = 69) }
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


@OptIn(ExperimentalMaterial3Api::class)
private fun finishTrip(
    leaderboardsViewModel: StiturLeaderboardsViewModel,
    loggedInLeaderboardEntry: MutableState<LeaderboardEntry>,
    locationRequest: MutableState<LocationRequest?>,
    newTripHistoryState: MutableState<TripHistory?>,
    gpsTripState: MutableState<Trip?>,
    loggedInProfile: MutableState<Profile>,
    profileViewModel: ProfileViewModel,
    ongoingTripState: MutableState<Trip?>,
    scope: CoroutineScope,
    sheetState: SheetState,
    toggleBottomSheet: (Boolean) -> Unit,
    selectedTripState: MutableState<Trip?>
) {
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
    val distance = gpsTripState.value?.let {
        newTripHistoryState.value?.trackedTrip = gpsTripState.value!!
        calculateDistanceKM(it.coordinates)
    }
    if (distance != null) {
        newTripHistoryState.value?.trackedDistanceKm = distance
    }

    newTripHistoryState.value?.let {
        newTripHistoryState.value!!.pointsEarned =
            (1000 * newTripHistoryState.value!!.trackedDistanceKm + 0.5).toInt()
        loggedInLeaderboardEntry.value.personalRanking.totalPoints += newTripHistoryState.value!!.pointsEarned
        leaderboardsViewModel.updateLeaderboardEntry(loggedInLeaderboardEntry.value)

        val temp: MutableList<TripHistory> = loggedInProfile.value.tripHistory.toMutableList()
        temp.add(newTripHistoryState.value!!)
        loggedInProfile.value.tripHistory = temp.toList()
        profileViewModel.updateUser(loggedInProfile.value)
    }
    newTripHistoryState.value = null
    ongoingTripState.value = null
    gpsTripState.value = null
    scope.launch { sheetState.hide() }.invokeOnCompletion {
        if (!sheetState.isVisible) {
            toggleBottomSheet(false)
            selectedTripState.value = null
        }
    }
}

@Composable
private fun TripOverview(selectedTripState: MutableState<Trip?>) {
    selectedTripState.value?.let { selectedTrip ->
        Text("Selected Trip: ${selectedTrip.routeName}")
        Text("Description: ${selectedTrip.routeDescription}")
        Text("Difficulty: ${selectedTrip.difficulty}")
        Text("Length: ${selectedTrip.lengthInMeters} meters")
    }
}


