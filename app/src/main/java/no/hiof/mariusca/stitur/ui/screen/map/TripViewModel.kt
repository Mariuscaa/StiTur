package no.hiof.mariusca.stitur.ui.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.service.storage.TripStorageService
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(private val tripStorageService: TripStorageService) :
    ViewModel() {
    val trips = tripStorageService.trips
    val filteredTrips = mutableListOf<Trip>()
    val allTrips = mutableListOf<Trip>()

    init {
        viewModelScope.launch {
            allTrips.addAll(tripStorageService.getName(""))
        }
    }

    fun createTrip(trip: Trip) {
        viewModelScope.launch {
            tripStorageService.save(trip)
        }
    }

    // henter ut absolutt alle trips, fiks så den henter kun den søkt på turen.
    // annen måte slik at du kun henter ut den du ønsker.
    fun getCreatedTrip(tripName: String) {
        viewModelScope.launch {
            //ChatGbt eksempel under.

            filteredTrips.clear()

            allTrips.forEach { trip ->
                if (trip.routeName.lowercase().contains(tripName))
                    filteredTrips.add(trip)
            }


            /*filteredTrips.clear()

            trips.collect() {
                    tripList -> tripList.forEach {
                        trip ->
                        if (trip.routeName.contains(tripName))
                            filteredTrips.add(trip)
                     }
            }*/
            //tripStorageService.getName(tripName)
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            tripStorageService.delete(trip.uid)
        }
    }
}

