package no.hiof.mariusca.stitur.signup

import androidx.annotation.StringRes

data class SignUpUiState (
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    @StringRes val errorMessage: Int = 0
)