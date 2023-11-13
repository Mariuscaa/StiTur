package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.home.Screen

@Composable
fun ProfileScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    profViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    profViewModel.getUserInfo(viewModel.currentLoggedInUserId)
    val filteredUser = profViewModel.filteredUsers

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.wrapContentSize()
        ) {
            ProfileItem(drawableId = R.drawable._icon__person_, text = filteredUser.value.username.uppercase())
            ProfileItem(drawableId = R.drawable._icon__history_, text = "Trip History")
            ProfileItem(drawableId = R.drawable._icon__camera_slr_, text = "My GeoTreasures")
            LogoutButton(viewModel, navController)
        }
    }
}

@Composable
fun ProfileItem(drawableId: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentSize()
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
