package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.service.storage.GeoTreasureStorageService
import javax.inject.Inject

class GeoTreasureStorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : GeoTreasureStorageService {


    override val treasures: Flow<List<GeoTreasure>>
        get() = firestore.collection(TREASURE_INFO_COLLECTION).dataObjects()
    override suspend fun delete(geoTreasureID: String) {
        firestore.collection(TREASURE_INFO_COLLECTION).document(geoTreasureID).delete().await()
    }


    override suspend fun save(treasure: GeoTreasure): String =
        firestore.collection(TREASURE_INFO_COLLECTION).add(treasure).await().id
    override suspend fun getGeoTreasure(geoTreasureID: String): GeoTreasure? =
        firestore.collection(TREASURE_INFO_COLLECTION).document(geoTreasureID).get().await().toObject()


    override suspend fun update(treasure: GeoTreasure) {
        firestore.collection(TREASURE_INFO_COLLECTION).document(treasure.geoTreasureID).set(treasure).await()
    }


    companion object {
        private const val TREASURE_INFO_COLLECTION = "GeoTreasure"
    }

}