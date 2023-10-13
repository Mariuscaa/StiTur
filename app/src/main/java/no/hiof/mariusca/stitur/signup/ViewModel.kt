package no.hiof.mariusca.stitur.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.service.module.AccountService
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(private val accountService: AccountService)
    : ViewModel() {

    init {
        createAnonymousAccount()
    }

    fun createAnonymousAccount() {
        viewModelScope.launch {
            if (!accountService.hasUser)
                accountService.createAnonymousAccount()
        }
    }
}