package no.hiof.mariusca.stitur.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.home.Screen

@Composable
fun LogoutButton(
    viewModel: SignUpViewModel, navController: NavController
) {
    Button(
        onClick = {
            viewModel.onSignOutClick()
            navController.navigate(route = Screen.SignIn.route) {

            }
        }, modifier = Modifier.padding(start = 25.dp, top = 16.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.mingcute_power_fill),
            contentDescription = "Power Icon",
            modifier = Modifier.size(36.dp)
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Text(text = "Sign Out", fontSize = 24.sp)
    }
}