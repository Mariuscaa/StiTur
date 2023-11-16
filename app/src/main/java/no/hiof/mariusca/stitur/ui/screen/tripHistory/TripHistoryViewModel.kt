package no.hiof.mariusca.stitur.ui.screen.tripHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.service.storage.TripHistoryStorageService
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(private val tripHistoryStorageService: TripHistoryStorageService) :
    ViewModel() {
    //val tripHistories = flowOf(listOf(TripHistory("test1"), TripHistory("test2")))


  //  val tripHistories = tripHistoryStorageService.tripHistories .onEach { tripHistoryList ->
    //    Log.d("TripHistoryViewModel", "Emitting trip history: $tripHistoryList")
    //}

    fun getTripHistoriesForUser(userId: String): Flow<List<TripHistory>> {
        return tripHistoryStorageService.getTripHistoriesForUser(userId)
    }

     /*
    val tripHistories = tripHistoryStorageService.tripHistories
        .catch { e ->
            Log.e("TripHistoryViewModel", "Error in trip history flow", e)
        }
        .onEach {  }
*/
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