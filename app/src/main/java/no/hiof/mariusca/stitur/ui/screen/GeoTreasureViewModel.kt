package no.hiof.mariusca.stitur.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.service.storage.GeoTreasureStorageService
import javax.inject.Inject


@HiltViewModel
class GeoTreasureViewModel @Inject constructor (private val geoTreasureStorageService : GeoTreasureStorageService) :
    ViewModel(){

    fun createTreasure(treasure: GeoTreasure) {
        viewModelScope.launch {
            geoTreasureStorageService.save(treasure)
        }
    }
}
