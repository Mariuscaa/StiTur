package no.hiof.mariusca.stitur.ui.screen.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.hiof.mariusca.stitur.model.GeoLocation
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.model.MinimalProfile
import no.hiof.mariusca.stitur.service.storage.GeoTreasureStorageService
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GeoTreasureViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GeoTreasureViewModel
    private val geoTreasureStorageService = mockk<GeoTreasureStorageService>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GeoTreasureViewModel(geoTreasureStorageService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun createTreasure_CallsSaveOnGeoTreasureStorageService() = runTest {
        val mockTreasure = GeoTreasure(
            uid = "uniqueId",
            title = "Treasure Title",
            textContent = "Description of Treasure",
            pictureUrl = "http://example.com/picture.jpg",
            geoLocation = GeoLocation(longitude = "59.911491", latitude = "10.757933"),
            madeBy = MinimalProfile(userID = "user123", userName = "TestUser")
        )

        viewModel.createTreasure(mockTreasure)
        advanceUntilIdle()
        coVerify { geoTreasureStorageService.save(mockTreasure) }
    }

    @Test
    fun deleteTreasure_CallsDeleteOnGeoTreasureStorageService() = runTest {
        val mockTreasure = GeoTreasure(
            uid = "uniqueId",
            title = "Treasure Title",
            textContent = "Description of Treasure",
            pictureUrl = "http://example.com/picture.jpg",
            geoLocation = GeoLocation(longitude = "59.911491", latitude = "10.757933"),
            madeBy = MinimalProfile(userID = "user123", userName = "TestUser")
        )

        viewModel.deleteTreasure(mockTreasure)
        advanceUntilIdle()
        coVerify { geoTreasureStorageService.delete(mockTreasure) }
    }
}