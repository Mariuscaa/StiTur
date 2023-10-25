package no.hiof.mariusca.stitur.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.common.ext.isValidEmail
import no.hiof.mariusca.stitur.common.ext.isValidPassword
import no.hiof.mariusca.stitur.service.module.AccountService
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val accountService: AccountService) : ViewModel() {

    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    val isAnonymous = accountService.currentUser.map { it.isAnonymous }


    fun createAnonymousAccount() {
        viewModelScope.launch {
            if (!accountService.hasUser)
                accountService.createAnonymousAccount()
        }
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }


    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onLoginClick() {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = R.string.email_error)
            return
        }

        else if (!password.isValidPassword()) {
            uiState.value = uiState.value.copy(errorMessage = R.string.password_error)
            return
        }


                viewModelScope.launch {
                    try {
                        accountService.authenticate(email, password) { error ->
                            if (error == null)
                                return@authenticate
                        }
                    }
                    catch(e: Exception) {
                        uiState.value = uiState.value.copy(errorMessage = R.string.could_not_log_in)
                    }
                }

    }

    fun onSignUpClick() {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(errorMessage = R.string.email_error)
            return
        }

        else if (!password.isValidPassword()) {
            uiState.value = uiState.value.copy(errorMessage = R.string.password_error)
            return
        }

        else if ((password != uiState.value.repeatPassword)) {
            uiState.value = uiState.value.copy(errorMessage = R.string.password_match_error)
            return
        }
                viewModelScope.launch {
                    try {
                        accountService.linkAccount(email, password) { error ->
                            if (error == null)
                                return@linkAccount
                        }
                    }
                    catch(e: Exception) {
                        uiState.value = uiState.value.copy(errorMessage = R.string.could_not_create_account)
                    }
                }
    }

    fun onSignOutClick() {
        viewModelScope.launch {
            accountService.signOut()
        }
    }
}