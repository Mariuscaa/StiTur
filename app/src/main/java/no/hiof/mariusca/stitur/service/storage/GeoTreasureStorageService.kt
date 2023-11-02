package no.hiof.mariusca.stitur.service.storage

import no.hiof.mariusca.stitur.model.GeoTreasure


interface GeoTreasureStorageService {

    suspend fun getGeoTreasure(geoTreasureID: String): GeoTreasure?
    suspend fun save(treasure: GeoTreasure): String
    suspend fun update(treasure: GeoTreasure)
    suspend fun delete(geoTreasureID: String)

}





