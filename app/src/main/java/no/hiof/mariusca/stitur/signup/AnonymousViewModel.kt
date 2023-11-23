package no.hiof.mariusca.stitur.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.service.module.AccountService
import javax.inject.Inject

// Not in use.
@HiltViewModel
class AnonymousViewModel @Inject constructor(private val accountService: AccountService)
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