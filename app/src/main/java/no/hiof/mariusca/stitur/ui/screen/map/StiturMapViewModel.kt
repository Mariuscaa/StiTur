package no.hiof.mariusca.stitur.ui.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.service.storage.TripStorageService
import javax.inject.Inject

@HiltViewModel
class StiturMapViewModel @Inject constructor(private val tripStorageService: TripStorageService) :
    ViewModel() {
    fun createTrip(routeName: String) {
        viewModelScope.launch {
            tripStorageService.save(Trip(routeName = routeName))
        }
    }

    val trips = tripStorageService.trips
}
