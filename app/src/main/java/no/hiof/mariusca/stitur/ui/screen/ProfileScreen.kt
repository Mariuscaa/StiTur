package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import no.hiof.mariusca.stitur.signup.SignUpViewModel


@Composable
fun ProfileScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Button(
            onClick = { viewModel.onSignOutClick() },
            modifier = Modifier.padding(16.dp, 8.dp),
        ) {
            Text(text = "Sign Out", fontSize = 16.sp)
        }
    }
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center)
    {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineLarge

        )
    }
}