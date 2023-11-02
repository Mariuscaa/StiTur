package no.hiof.mariusca.stitur.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.service.storage.GeoTreasureStorageService
import javax.inject.Inject

class GeoTreasureStorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : GeoTreasureStorageService {

    override suspend fun delete(geoTreasureID: String) {
        TODO("Not yet implemented")
    }
    override suspend fun save(treasure: GeoTreasure): String =
        firestore.collection(GeoTreasureStorageServiceImpl.TREASURE_INFO_COLLECTION).add(treasure).await().id
    override suspend fun getGeoTreasure(geoTreasureID: String): GeoTreasure? =
        firestore.collection(GeoTreasureStorageServiceImpl.TREASURE_INFO_COLLECTION).document(geoTreasureID).get().await().toObject()


    override suspend fun update(treasure: GeoTreasure) {
        TODO("Not yet implemented")
    }


    companion object {
        private const val TREASURE_INFO_COLLECTION = "GeoTreasure"
    }

}