package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.service.storage.TripStorageService
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject

class TripStorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : TripStorageService {

    override val trips: Flow<List<Trip>>
        get() = firestore.collection(TRIPS_COLLECTION).dataObjects()

    override suspend fun get(tripId: String): Trip? =
        firestore.collection(TRIPS_COLLECTION).document(tripId).get().await().toObject()


    override suspend fun save(trip: Trip): String =
        firestore.collection(TRIPS_COLLECTION).add(trip).await().id

    override suspend fun update(trip: Trip) {
        val tripDocument = firestore.collection(TRIPS_COLLECTION).document(trip.uid)
        tripDocument.set(trip).await()
    }

    override suspend fun delete(tripId: String) {
        firestore.collection(TRIPS_COLLECTION).document(tripId).delete().await()
    }

    companion object {
        private const val TRIPS_COLLECTION = "GeoJsonFeatures"
    }
}