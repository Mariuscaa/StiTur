package no.hiof.mariusca.stitur.ui.screen

import androidx.annotation.StringRes
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.home.Screen


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signViewModel: SignUpViewModel = hiltViewModel(),
    profViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController

) {
    val uiState by signViewModel.uiState
    val isAnonymous by signViewModel.isAnonymous.collectAsState(initial = true)
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

            EmailField(uiState.email, signViewModel::onEmailChange, fieldModifier)
            UserNameField(uiState.userName, signViewModel::onUserNameChange, fieldModifier )
            PasswordField(uiState.password, signViewModel::onPasswordChange, fieldModifier)

            RepeatPasswordField(uiState.repeatPassword, signViewModel::onRepeatPasswordChange, fieldModifier)

            Row {
                Button(
                    // signViewModel.onLoginClick()
                    onClick = { navController.navigate(Screen.SignIn.route)},
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                ) {
                    Text(text = stringResource(R.string.login), fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        signViewModel.onSignUpClick { userId ->
                            if (userId != null) {
                                val newProfile = Profile(userID = userId, false,signViewModel.uiState.value.userName)
                                profViewModel.createUser(newProfile)
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
                onClick = {signViewModel.createAnonymousAccount()},
                modifier = Modifier
                    .padding(16.dp, 8.dp),)
            {
                Text(text = ("Continue as Anonymous"), fontSize = 16.sp)
            }
        }
    }
    else {

        navController.navigate(route = Screen.StiturMap.route)

    }
}

@Composable
fun UserNameField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier){
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(R.string.user_name)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Face, contentDescription = "Email") }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(R.string.email)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") }
    )
}

@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    PasswordField(value, R.string.password, onNewValue, modifier)
}

@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PasswordField(value, R.string.repeat_password, onNewValue, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon =
        //Den drawablen under skal endres til å bli noe annet
        if (isVisible) painterResource(R.drawable.powerbutton)
        //Den drawablen under skal endres til å bli noe annet
        else painterResource(R.drawable.eye)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}