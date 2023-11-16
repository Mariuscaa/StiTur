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
    var filteredUser = mutableStateOf(Profile())


    fun getUserInfo(user: String) {
        viewModelScope.launch {
            val temp = userInfoStorageService.getProfile(user)

            if (temp != null) {
                filteredUser.value = temp
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

    fun updateUsername(userId: String, newUsername: String) {
        viewModelScope.launch {
            val currentUserProfile = filteredUser.value.copy(username = newUsername)
            userInfoStorageService.update(currentUserProfile)
            getUserInfo(userId)
        }
    }
}
