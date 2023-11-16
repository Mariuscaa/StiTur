package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.home.Screen


@Composable
fun ProfileScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    profViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
   profViewModel.getUserInfo(viewModel.currentLoggedInUserId)
    val filteredUser = profViewModel.filteredUser


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
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
            modifier = Modifier.padding(16.dp, 8.dp),

        ) {
            Text(text = "Sign Out", fontSize = 16.sp)
        }
    }
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        //Kan legge til mer her for å endre passord og Username
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineLarge

        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding()) {
            Text(text = "Welcome, ${filteredUser.value.username}", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Display the list of trips
            //Text(text = "Trip History:", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
            //filteredUser.tripHistory.forEach { tripHistory ->
                // Add your trip history item composable here
            }

        }
    }
//}