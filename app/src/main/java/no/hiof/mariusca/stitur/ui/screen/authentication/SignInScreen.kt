package no.hiof.mariusca.stitur.ui.screen.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.home.Screen


@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    signInViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by signInViewModel.uiState
    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 4.dp)

    if (signInViewModel.currentLoggedInUserId.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.errorMessage != 0)
                Text(
                    text = stringResource(id = uiState.errorMessage),
                    Modifier.padding(vertical = 8.dp)
                )

            EmailField(uiState.email, signInViewModel::onEmailChange, fieldModifier)
            PasswordFieldSignIn(uiState.password, signInViewModel::onPasswordChange, fieldModifier)

            Row {
                Button(
                    onClick = { signInViewModel.onLoginClick() },
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                ) {
                    Text(text = stringResource(R.string.login), fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        navController.navigate(Screen.SignUp.route)
                    },
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                ) {
                    Text(text = stringResource(R.string.create_account), fontSize = 16.sp)
                }
            }

        }
    }
}

@Composable
private fun PasswordFieldSignIn(value: String, onNewValue: (String) -> Unit, modifier: Modifier) {
    var isVisible by remember { mutableStateOf(false) }

    val (icon, size) = if (isVisible) {
        Pair(painterResource(R.drawable.eye), 34.dp)
    } else {
        Pair(painterResource(R.drawable.closedeye), 24.dp)
    }

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(R.string.password)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Image(
                    painter = icon,
                    contentDescription = "Toggle visibility",
                    modifier = Modifier.size(size)
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}
