package no.hiof.mariusca.stitur.service.storage

import kotlinx.coroutines.flow.Flow
import no.hiof.mariusca.stitur.model.LeaderboardEntry

/** This interface describes the possible operations that can be performed on the leaderboard user objects.
Async operations handled by using Flow**/
interface LeaderboardsService {
    val leaderboardEntries: Flow<List<LeaderboardEntry>>
    suspend fun get(leaderboardEntryId: String): LeaderboardEntry?
    suspend fun save(leaderboardEntry: LeaderboardEntry): String
    suspend fun update(leaderboardEntry: LeaderboardEntry)
    suspend fun delete(leaderboardEntryId: String)
}