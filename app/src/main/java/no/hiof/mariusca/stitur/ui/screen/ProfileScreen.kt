package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.home.Screen

@Composable
fun ProfileScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    profViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {

    val isEditMode = remember { mutableStateOf(false) }
    val showTripHistoryScreen = remember { mutableStateOf(false) }
    val newUsername = remember { mutableStateOf("") }

    profViewModel.getUserInfo(viewModel.currentLoggedInUserId)
    val filteredUser = profViewModel.filteredUser



    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(
            onClick = { isEditMode.value = !isEditMode.value },
            modifier = Modifier.size(48.dp)
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

    Box(
        modifier = Modifier.fillMaxSize().padding(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.wrapContentSize()
        ) {


            if (isEditMode.value) {
                EditUsernameView(
                    currentUsername = filteredUser.value.username,
                    newUsername = newUsername,
                    userId = viewModel.currentLoggedInUserId,
                    viewModel = profViewModel,
                    onUsernameUpdated = {
                        isEditMode.value = false
                    }
                )
            } else {
                ProfileItem(
                    drawableId = R.drawable._icon__person_,
                    text = filteredUser.value.username.uppercase(),
                    onClick = {
                        showTripHistoryScreen.value = !showTripHistoryScreen.value

                    }

                )




                ProfileItem(drawableId = R.drawable._icon__history_, text = "Trip History",
                    onClick = {
                    showTripHistoryScreen.value = !showTripHistoryScreen.value

                })
                if (showTripHistoryScreen.value) {
                    TripHistoryScreen(filteredUser.value
                    )

                }
                ProfileItem(drawableId = R.drawable._icon__camera_slr_, text = "My GeoTreasures")
                LogoutButton(viewModel, navController)
            }
        }
    }
}



@Composable
fun EditUsernameView(
    currentUsername: String,
    newUsername: MutableState<String>,
    userId: String,
    viewModel: ProfileViewModel,
    onUsernameUpdated: () -> Unit
) {
    Column {
        Text("Current Username: $currentUsername", style = MaterialTheme.typography.bodyMedium)
        TextField(
            value = newUsername.value,
            onValueChange = { newUsername.value = it },
            label = { Text("New Username") }
        )
        Button(onClick = {
            viewModel.updateUsername(userId, newUsername.value)
            onUsernameUpdated()
        }) {
            Text("Update Username")
        }
    }

}


@Composable
fun ProfileItem(drawableId: Int, text: String, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentSize()
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp))
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun LogoutButton(
    viewModel: SignUpViewModel,
    navController: NavController
) {
    Button(
        onClick = {
            viewModel.onSignOutClick()
            navController.navigate(route = Screen.SignIn.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        },
        modifier = Modifier
            .padding(start = 25.dp, top = 16.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.mingcute_power_fill),
            contentDescription = "Power Icon",
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier
            .width(8.dp))
        Text(text = "Sign Out", fontSize = 24.sp)
    }
}

@Composable
fun TripHistoryCard(tripHistory: TripHistory) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.maps),
                contentDescription = "Map Thumbnail",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = tripHistory.trackedTrip.routeName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Points",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = "${tripHistory.pointsEarned}")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Distance",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = "${tripHistory.trackedDistanceKm} km")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Duration",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = "${tripHistory.durationMinutes} min")
                }
            }
        }
    }
}

@Composable
fun TripHistoryScreen(loggedInProfile: Profile) {
    Column {
          if (loggedInProfile.tripHistory.isEmpty()){
              Text(
                  text = "You have no trips to show",
                  style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                  )
          }
            else{
              LazyColumn {
              items(loggedInProfile.tripHistory) { tripHistory ->
                  TripHistoryCard(tripHistory = tripHistory)
              }
              }
            }
    }
}




@Composable
fun GeoTreasureScreen(){

}


