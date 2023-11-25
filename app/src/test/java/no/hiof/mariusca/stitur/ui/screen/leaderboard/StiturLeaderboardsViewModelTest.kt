package no.hiof.mariusca.stitur.ui.screen.leaderboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.hiof.mariusca.stitur.model.LeaderboardEntry
import no.hiof.mariusca.stitur.model.PersonalRanking
import no.hiof.mariusca.stitur.model.Tiers
import no.hiof.mariusca.stitur.service.storage.LeaderboardsService
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StiturLeaderboardsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val leaderboardsService = mockk<LeaderboardsService>(relaxed = true)
    private lateinit var viewModel: StiturLeaderboardsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StiturLeaderboardsViewModel(leaderboardsService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getUserInfo_UpdatesFilteredUserWhenValidDataReturned() = runTest {
        val testId = "testId"
        val testEntry = LeaderboardEntry(uid = testId)
        coEvery { leaderboardsService.get(testId) } returns testEntry

        viewModel.getUserInfo(testId)
        advanceUntilIdle()

        assertEquals(testEntry, viewModel.filteredUser.value)
    }

    @Test
    fun deleteLeaderboardEntry_CallsServiceDeleteWithCorrectId() = runTest {
        val testEntry = LeaderboardEntry(uid = "testId")

        viewModel.deleteLeaderboardEntry(testEntry)
        advanceUntilIdle()

        coVerify { leaderboardsService.delete(testEntry.uid) }
    }

    @Test
    fun createLeaderboardEntry_CallsServiceSaveWithCorrectEntry() = runTest {
        val testEntry = LeaderboardEntry(uid = "newId")

        viewModel.createLeaderboardEntry(testEntry)
        advanceUntilIdle()

        coVerify { leaderboardsService.save(testEntry) }
    }

    @Test
    fun getLeaderboardEntry_FiltersAndSortsEntriesCorrectly() = runTest {
        val entries = listOf(
            LeaderboardEntry(uid = "1", username = "user1", personalRanking = PersonalRanking(Tiers.GOLD, 100)),
            LeaderboardEntry(uid = "2", username = "user2", personalRanking = PersonalRanking(Tiers.SILVER, 80))
        )
        coEvery { leaderboardsService.getLeaderboardUsername("") } returns entries

        viewModel.getLeaderboardEntry("user1", Tiers.GOLD)
        advanceUntilIdle()

        with(viewModel.filteredLeaderboards) {
            assertEquals(1, size)
            assertEquals("user1", first().username)
            assertEquals(Tiers.GOLD, first().personalRanking.tier)
        }
    }

    @Test
    fun updateLeaderboardEntry_CallsServiceUpdateWithCorrectEntry() = runTest {
        val testEntry = LeaderboardEntry(uid = "updateId")

        viewModel.updateLeaderboardEntry(testEntry)
        advanceUntilIdle()

        coVerify { leaderboardsService.update(testEntry) }
    }
}