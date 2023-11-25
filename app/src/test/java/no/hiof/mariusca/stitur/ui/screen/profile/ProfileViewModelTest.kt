package no.hiof.mariusca.stitur.ui.screen.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.service.storage.ProfileStorageService
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {


    @get:Rule //Inspirasjon fra: https://junit.org/junit4/javadoc/4.12/org/junit/Rule.html
    var instantExecutorRule = InstantTaskExecutorRule() //Inspirasjon fra: https://developer.android.com/reference/androidx/arch/core/executor/testing/InstantTaskExecutorRule

    private lateinit var viewModel: ProfileViewModel
    private val profileInfoStorageService = mockk<ProfileStorageService>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher() //Inspirasjon fra: https://junit.org/junit4/javadoc/4.12/org/junit/Before.html


    @Before //Inspirasjon fra: https://junit.org/junit4/javadoc/4.12/org/junit/Before.html
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel(profileInfoStorageService)
    }


    @After//Inspirasjon fra: https://junit.org/junit4/javadoc/4.12/org/junit/After.html
    fun tearDown() {
        Dispatchers.resetMain() //Inspirasjon fra: https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/
    }

    @Test
    fun getUserInfo_WithExistingProfile_UpdatesFilteredUser() = runTest {
        val testUser = "testUser"
        val testProfile = Profile(userID = testUser, )
        coEvery { profileInfoStorageService.getProfile(testUser) } returns testProfile //Inspirasjon fra: https://mockk.io/

        viewModel.getUserInfo(testUser)

        advanceUntilIdle() //Inspirasjon fra: https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/
        assert(viewModel.filteredUser.value == testProfile)
    }
    @Test
    fun getUserInfo_WithNullProfile_DoesNotUpdateFilteredUser() = runTest {
        val testUser = "testUser"
        coEvery { profileInfoStorageService.getProfile(testUser) } returns null

        viewModel.getUserInfo(testUser)

        assert(viewModel.filteredUser.value == Profile())
    }

    @Test
    fun createUser_CallsSaveOnProfileStorageService() = runTest {
        val testProfile = Profile(userID = "newUser")

        viewModel.createUser(testProfile)
        advanceUntilIdle()
        coVerify { profileInfoStorageService.save(testProfile) }
    }

    @Test
    fun updateUser_CallsUpdateOnProfileStorageService() = runTest {
        val testProfile = Profile(userID = "existingUser")

        viewModel.updateUser(testProfile)
        advanceUntilIdle()
        coVerify { profileInfoStorageService.update(testProfile) }
    }

    @Test
    fun updateUsername_ChangesUsernameAndRefreshesUserInfo() = runTest {
        val userId = "user123"
        val newUsername = "newUsername"
        val initialProfile = Profile(userID = userId, username = "oldUsername")
        viewModel.filteredUser.value = initialProfile
        assert(viewModel.filteredUser.value.username == "oldUsername")
        coEvery { profileInfoStorageService.update(any()) } just Runs
        coEvery { profileInfoStorageService.getProfile(userId) } returns Profile(userID = userId, username = newUsername)

        viewModel.updateUsername(userId, newUsername)
        advanceUntilIdle()
        coVerify { profileInfoStorageService.update(Profile(userID = userId, username = newUsername)) }
        coVerify { profileInfoStorageService.getProfile(userId) }
        assert(viewModel.filteredUser.value.username == newUsername)
    }
}
