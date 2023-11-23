package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.service.storage.GeoTreasureStorageService
import javax.inject.Inject

class GeoTreasureStorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : GeoTreasureStorageService {
    override val treasures: Flow<List<GeoTreasure>>
        get() = firestore.collection(TREASURE_INFO_COLLECTION).dataObjects()

    override suspend fun delete(treasure: GeoTreasure) {
        // Connected with profiles so it also deleted from profile.
        val profile = firestore.collection("UserInfo")
            .document(treasure.madeBy.userID).get().await().toObject<Profile>()
        val temp = profile?.geoTreasures?.toMutableList()
        temp?.removeIf { it.title == treasure.title }

        if (profile != null) {
            if (temp != null) {
                profile.geoTreasures = temp.toList()
            }
        }
        if (profile != null) {
            firestore.collection("UserInfo").document(treasure.madeBy.userID).set(profile)
                .await()
        }

        firestore.collection(TREASURE_INFO_COLLECTION).document(treasure.uid).delete().await()
    }

    override suspend fun save(treasure: GeoTreasure): String {
        // Connected with profiles so it also saves to profile.
        val profile = firestore.collection("UserInfo")
            .document(treasure.madeBy.userID).get().await().toObject<Profile>()
        val temp = profile?.geoTreasures?.toMutableList()
        temp?.add(treasure)

        if (profile != null) {
            if (temp != null) {
                profile.geoTreasures = temp.toList()
            }
        }
        if (profile != null) {
            firestore.collection("UserInfo").document(treasure.madeBy.userID).set(profile)
                .await()
        }
        return firestore.collection(TREASURE_INFO_COLLECTION).add(treasure).await().id

    }

    override suspend fun getGeoTreasure(geoTreasureID: String): GeoTreasure? =
        firestore.collection(TREASURE_INFO_COLLECTION).document(geoTreasureID).get().await()
            .toObject()

    override suspend fun update(treasure: GeoTreasure) {
        firestore.collection(TREASURE_INFO_COLLECTION).document(treasure.uid).set(treasure).await()
    }

    companion object {
        private const val TREASURE_INFO_COLLECTION = "GeoTreasure"
    }
}