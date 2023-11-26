package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.LeaderboardEntry
import no.hiof.mariusca.stitur.service.storage.LeaderboardsService
import javax.inject.Inject

class LeaderboardsServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : LeaderboardsService {
    override val leaderboardEntries: Flow<List<LeaderboardEntry>>
        get() = firestore.collection(LEADERBOARDS_DATA).dataObjects()

    override suspend fun get(leaderboardEntryId: String): LeaderboardEntry? =
        firestore.collection(LEADERBOARDS_DATA).document(leaderboardEntryId).get().await()
            .toObject()
    override suspend fun save(leaderboardEntry: LeaderboardEntry): String =
        firestore.collection(LEADERBOARDS_DATA).add(leaderboardEntry).await().id

    override suspend fun update(leaderboardEntry: LeaderboardEntry) {
        val leaderboardDocument =
            firestore.collection(LEADERBOARDS_DATA).document(leaderboardEntry.uid)
        leaderboardDocument.set(leaderboardEntry).await()
    }
    override suspend fun delete(leaderboardEntryId: String) {
        firestore.collection(LEADERBOARDS_DATA).document(leaderboardEntryId).delete().await()
    }
    override suspend fun getLeaderboardUsername(username: String): List<LeaderboardEntry> =
        firestore.collection(LEADERBOARDS_DATA).get().await().toObjects()
    companion object {
        private const val LEADERBOARDS_DATA = "LeaderboardEntry"
    }
}