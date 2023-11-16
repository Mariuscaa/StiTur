package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.TripHistory
import no.hiof.mariusca.stitur.service.storage.TripHistoryStorageService
import javax.inject.Inject
class TripHistoryStorageServiceImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    TripHistoryStorageService {

   // override val tripHistories: Flow<List<TripHistory>>
   //     get() = firestore.collection(TRIP_HISTORY_COLLECTION).dataObjects()


    override fun getTripHistoriesForUser(userId: String): Flow<List<TripHistory>> = callbackFlow {
        val tripHistoryCollection = firestore.collection(USER_INFO_COLLECTION)
            .document(userId)
            .collection(TRIP_HISTORY_SUBCOLLECTION)

        val subscription = tripHistoryCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val tripHistories = snapshot?.toObjects(TripHistory::class.java)
            trySend(tripHistories ?: emptyList())
        }

        awaitClose { subscription.remove() }
    }
    override suspend fun get(tripHistoryId: String): TripHistory? =
        firestore.collection(TRIP_HISTORY_SUBCOLLECTION).document(tripHistoryId).get().await()
            .toObject()

    override suspend fun save(tripHistory: TripHistory): String =
        firestore.collection(TRIP_HISTORY_SUBCOLLECTION).add(tripHistory).await().id

    override suspend fun update(tripHistory: TripHistory) {
        val tripDocument = firestore.collection(TRIP_HISTORY_SUBCOLLECTION).document(tripHistory.uid)
        tripDocument.set(tripHistory).await()
    }

    override suspend fun delete(tripHistoryId: String) {
        firestore.collection(TRIP_HISTORY_SUBCOLLECTION).document(tripHistoryId).delete().await()
    }
    companion object {
        private const val USER_INFO_COLLECTION = "UserInfo"
        private const val TRIP_HISTORY_SUBCOLLECTION = "tripHistory"
    }
}