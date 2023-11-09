package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.service.storage.TripHistoryStorageService
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject
class TripHistoryStorageServiceImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    TripHistoryStorageService {
    override val tripHistories: Flow<List<TripHistory>>
        get() = firestore.collection(TRIP_HISTORY_COLLECTION).dataObjects()

    override suspend fun get(tripHistoryId: String): TripHistory? =
        firestore.collection(TRIP_HISTORY_COLLECTION).document(tripHistoryId).get().await()
            .toObject()

    override suspend fun save(tripHistory: TripHistory): String =
        firestore.collection(TRIP_HISTORY_COLLECTION).add(tripHistory).await().id

    override suspend fun update(tripHistory: TripHistory) {
        val tripDocument = firestore.collection(TRIP_HISTORY_COLLECTION).document(tripHistory.uid)
        tripDocument.set(tripHistory).await()
    }

    override suspend fun delete(tripHistoryId: String) {
        firestore.collection(TRIP_HISTORY_COLLECTION).document(tripHistoryId).delete().await()
    }
    companion object {
        private const val TRIP_HISTORY_COLLECTION = "TripHistory"
    }
}