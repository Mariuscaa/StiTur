package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.service.storage.UserInfoStorageService
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userInfoStorageService: UserInfoStorageService) :
    ViewModel() {
    var filteredUsers = mutableStateOf(Profile())
    fun getUserInfo(user: String) {
        viewModelScope.launch {

            val temp = userInfoStorageService.getProfile(user)

            if (temp != null) {
                filteredUsers.value = temp
            }


        }
    }

    fun createUser(user: Profile) {
        viewModelScope.launch {
            userInfoStorageService.save(user)
        }
    }

    fun updateUser (user: Profile) {
        viewModelScope.launch {
            userInfoStorageService.update(user)
        }
    }
}
