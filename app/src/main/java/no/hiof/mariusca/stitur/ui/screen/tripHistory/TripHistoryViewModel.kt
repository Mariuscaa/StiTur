package no.hiof.mariusca.stitur.ui.screen.tripHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.service.storage.TripHistoryStorageService
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(private val tripHistoryStorageService: TripHistoryStorageService) :
    ViewModel() {
    val tripHistories = tripHistoryStorageService.tripHistories

    fun createTripHistory(tripHistory: TripHistory) {
        viewModelScope.launch {
            tripHistoryStorageService.save(tripHistory)
        }
    }
    fun deleteTripHistory(tripHistory: TripHistory) {
        viewModelScope.launch {
            tripHistoryStorageService.delete(tripHistory.uid)
        }
    }

}