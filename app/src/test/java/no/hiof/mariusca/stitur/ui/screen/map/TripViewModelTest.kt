package no.hiof.mariusca.stitur.ui.screen.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.service.storage.TripStorageService
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class TripViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TripViewModel
    private val tripStorageService = mockk<TripStorageService>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TripViewModel(tripStorageService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun createTrip_CallsSaveOnTripStorageService() = runTest {
        val mockTrip = Trip(uid = "testUid", routeName = "Test Route")

        viewModel.createTrip(mockTrip)
        advanceUntilIdle()
        coVerify { tripStorageService.save(mockTrip) }
    }


    @Test
    fun getFilteredTrips_WithSpecificName_FiltersTripsCorrectly() = runTest {
        val tripName = "tripName"
        val mockTripList = listOf(
            Trip(routeName = tripName, uid = "1"),
            Trip(routeName = "otherTripName", uid = "2")
        )


        coEvery { tripStorageService.getName("") } returns mockTripList


        viewModel = TripViewModel(tripStorageService)


        viewModel.getFilteredTrips(tripName)


        advanceUntilIdle()


        assert(viewModel.filteredTrips.size == 1)
        assert(viewModel.filteredTrips.first().routeName == tripName)
    }




    @Test
    fun deleteTrip_CallsDeleteOnTripStorageService() = runTest {
        val mockTrip = Trip(uid = "uniqueId", routeName = "Test Route")

        viewModel.deleteTrip(mockTrip)
        advanceUntilIdle()
        coVerify { tripStorageService.delete(mockTrip.uid) }
    }




}