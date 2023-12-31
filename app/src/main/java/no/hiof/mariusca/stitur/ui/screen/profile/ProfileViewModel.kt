package no.hiof.mariusca.stitur.ui.screen.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.service.storage.ProfileStorageService
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileStorageService: ProfileStorageService) :
    ViewModel() {
    var filteredUser = mutableStateOf(Profile())

    fun getUserInfo(user: String) {
        viewModelScope.launch {
            val temp = profileStorageService.getProfile(user)
            if (temp != null) {
                filteredUser.value = temp
            }
        }
    }

    fun createUser(user: Profile) {
        viewModelScope.launch {
            profileStorageService.save(user)
        }
    }

    fun updateUser (user: Profile) {
        viewModelScope.launch {
            profileStorageService.update(user)
        }
    }

    fun updateUsername(userId: String, newUsername: String) {
        viewModelScope.launch {
            val currentUserProfile = filteredUser.value.copy(username = newUsername)
            profileStorageService.update(currentUserProfile)
            getUserInfo(userId)
        }
    }
}
