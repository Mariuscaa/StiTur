package no.hiof.mariusca.stitur.service.storage

import kotlinx.coroutines.flow.Flow
import no.hiof.mariusca.stitur.model.Trip

interface TripStorageService {
    val trips: Flow<List<Trip>>
    suspend fun get(tripId: String): Trip?


    //må legge til en ny get for å kun hente ut navn (ikke noe trip id).
    suspend fun getName(routeName: String): List<Trip>

    suspend fun save(trip: Trip): String
    suspend fun update(trip: Trip)
    suspend fun delete(tripId: String)
}