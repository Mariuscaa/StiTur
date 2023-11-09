package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


//ScreenTest

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    signInViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by signInViewModel.uiState
    val isAnonymous by signInViewModel.isAnonymous.collectAsState(initial = true)
    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 4.dp)

    if (isAnonymous) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.errorMessage != 0)
                Text(text = stringResource(id = uiState.errorMessage),
                    Modifier.padding(vertical = 8.dp))

            EmailField(uiState.email, signInViewModel::onEmailChange, fieldModifier)
            PasswordField(uiState.password, signInViewModel::onPasswordChange, fieldModifier)

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
                        signInViewModel.onSignUpClick { userId ->
                            if (userId != null) {
                                navController.navigate(route = Screen.StiturMap.route)
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                ) {
                    Text(text = stringResource(R.string.create_account), fontSize = 16.sp)
                }
            }
            Button(
                onClick = { signInViewModel.createAnonymousAccount() },
                modifier = Modifier
                    .padding(16.dp, 8.dp),
            )
            {
                Text(text = ("Continue as Anonymous"), fontSize = 16.sp)
            }
        }
    } else {
        navController.navigate(route = Screen.StiturMap.route)
    }
}

@Composable
private fun String.EmailField(onNewValue: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = this,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(R.string.email)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}

@Composable
private fun PasswordFieldSignIn(value: String, onNewValue: (String) -> Unit, modifier: Modifier) {
    PasswordField(value, R.string.password, onNewValue, modifier)
}

@Composable
private fun PasswordField(
    value: String,
    placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier
) {
    var isVisible: Boolean by remember { mutableStateOf(false) }

    val icon =
        if (isVisible) Icons.Default.Lock else Icons.Default.Lock

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}
