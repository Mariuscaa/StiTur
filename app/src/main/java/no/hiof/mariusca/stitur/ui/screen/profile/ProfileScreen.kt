package no.hiof.mariusca.stitur.ui.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.signup.SignUpViewModel

@Composable
fun ProfileScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    val isEditMode = remember { mutableStateOf(false) }
    val showTripHistoryScreen = remember { mutableStateOf(false) }
    val showGeoTreasureScreen = remember { mutableStateOf(false) }
    val newUsername = remember { mutableStateOf("") }

    profileViewModel.getUserInfo(signUpViewModel.currentLoggedInUserId)
    val filteredUser = profileViewModel.filteredUser

    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd
    ) {
        EditButton(isEditMode)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start, modifier = Modifier.wrapContentSize()
        ) {
            if (isEditMode.value) {
                EditUsernameView(currentUsername = filteredUser.value.username,
                    newUsername = newUsername,
                    userId = signUpViewModel.currentLoggedInUserId,
                    viewModel = profileViewModel,
                    onUsernameUpdated = {
                        isEditMode.value = false
                    })
            } else {
                ProfileItem(drawableId = R.drawable._icon__person_,
                    text = filteredUser.value.username.uppercase(),
                    onClick = {
                        showTripHistoryScreen.value = !showTripHistoryScreen.value
                    }
                )

                ProfileItem(drawableId = R.drawable._icon__camera_slr_,
                    text = "My GeoTreasures",
                    onClick = {
                        showGeoTreasureScreen.value = !showGeoTreasureScreen.value
                    })
                if (showGeoTreasureScreen.value) {
                    GeoTreasureSection(
                        filteredUser.value
                    )
                }

                ProfileItem(drawableId = R.drawable._icon__history_,
                    text = "Trip History",
                    onClick = {
                        showTripHistoryScreen.value = !showTripHistoryScreen.value

                    })
                if (showTripHistoryScreen.value) {
                    TripHistorySection(
                        filteredUser.value
                    )

                }
                LogoutButton(signUpViewModel, navController)
            }
        }
    }
}

@Composable
private fun EditButton(isEditMode: MutableState<Boolean>) {
    IconButton(
        onClick = { isEditMode.value = !isEditMode.value }, modifier = Modifier.size(48.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (isEditMode.value) R.drawable.x else R.drawable.editicon
            ),
            contentDescription = if (isEditMode.value) "Done" else "Edit",
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun TripHistorySection(loggedInProfile: Profile) {
    Column {
        if (loggedInProfile.tripHistory.isEmpty()) {
            Text(
                text = "You have no trips to show",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
            )
        } else {
            LazyColumn {
                items(loggedInProfile.tripHistory) { tripHistory ->
                    TripHistoryCard(tripHistory = tripHistory)
                }
            }
        }
    }
}


@Composable
fun GeoTreasureSection(loggedInProfile: Profile) {
    Column {
        if (loggedInProfile.geoTreasures.isEmpty()) {
            Text(
                text = "You have no GeoTreasures to show",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
            )
        } else {
            LazyColumn {
                items(loggedInProfile.geoTreasures) { geoTreasureObj ->
                    GeoTreasureCard(geoTreasureObj = geoTreasureObj)
                }
            }
        }
    }
}


