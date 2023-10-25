package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import no.hiof.mariusca.stitur.signup.SignUpViewModel


@Composable
fun ProfileScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {



    Text(text = "Profile",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center)


    Row {
        Button(
            onClick = { viewModel.onSignOutClick() },
            modifier = Modifier
                .padding(16.dp, 8.dp),
        ) {
            Text(text = ("Sign Out"), fontSize = 16.sp)
        }
    }
}