package no.hiof.mariusca.stitur.service.storage

import kotlinx.coroutines.flow.Flow
import no.hiof.mariusca.stitur.model.GeoTreasure


interface GeoTreasureStorageService {

    val treasures: Flow<List<GeoTreasure>>
    suspend fun getGeoTreasure(geoTreasureID: String): GeoTreasure?
    suspend fun save(treasure: GeoTreasure): String
    suspend fun update(treasure: GeoTreasure)
    suspend fun delete(treasure: GeoTreasure)

}





