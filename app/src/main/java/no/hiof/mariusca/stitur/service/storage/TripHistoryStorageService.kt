package no.hiof.mariusca.stitur.service.storage

import kotlinx.coroutines.flow.Flow
import no.hiof.mariusca.stitur.model.TripHistory

interface TripHistoryStorageService {
    val tripHistories: Flow<List<TripHistory>>
    suspend fun get(tripHistoryId: String): TripHistory?
    suspend fun save(tripHistory: TripHistory): String
    suspend fun update(tripHistory: TripHistory)
    suspend fun delete(tripHistoryId: String)
}
