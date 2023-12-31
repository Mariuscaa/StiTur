package no.hiof.mariusca.stitur.ui.screen.leaderboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.LeaderboardEntry
import no.hiof.mariusca.stitur.model.Tiers
import no.hiof.mariusca.stitur.service.storage.LeaderboardsService
import javax.inject.Inject

@HiltViewModel
class StiturLeaderboardsViewModel @Inject constructor(private val leaderboardsService: LeaderboardsService) :
    ViewModel() {

    var filteredUser = mutableStateOf(LeaderboardEntry())
    fun getUserInfo(documentId: String) {
        viewModelScope.launch {
            val temp = leaderboardsService.get(documentId)

            if (temp != null) {
                filteredUser.value = temp
            }
        }
    }

    fun deleteLeaderboardEntry(leaderboardEntry: LeaderboardEntry) {
        viewModelScope.launch {
            leaderboardsService.delete(leaderboardEntry.uid)
        }
    }

    fun createLeaderboardEntry(leaderboardEntry: LeaderboardEntry) {
        viewModelScope.launch {
            leaderboardsService.save(leaderboardEntry)
        }
    }

    val leaderboardEntries: Flow<List<LeaderboardEntry>> = leaderboardsService.leaderboardEntries
    val filteredLeaderboards = mutableListOf<LeaderboardEntry>()
    val allLeaderboards = mutableListOf<LeaderboardEntry>()

    init {
        viewModelScope.launch {
            allLeaderboards.addAll(leaderboardsService.getLeaderboardUsername(""))
        }
    }

    // Prepared to add Tiers functionality here, but did not get that far in frontend.
    fun getLeaderboardEntry(leaderboardEntryUser: String?, tier: Tiers?) {
        viewModelScope.launch {
            filteredLeaderboards.clear()

            if (tier != null && tier != Tiers.ALL) {
                allLeaderboards.forEach { leaderboardEntry ->
                    if (leaderboardEntry.personalRanking.tier == tier) filteredLeaderboards.add(
                        leaderboardEntry
                    )
                }
                if (!leaderboardEntryUser.isNullOrEmpty()) {
                    filteredLeaderboards.forEach { leaderboardEntry ->
                        if (!leaderboardEntry.username.lowercase()
                                .contains(leaderboardEntryUser)
                        ) filteredLeaderboards.remove(leaderboardEntry)
                    }
                }
            } else if (!leaderboardEntryUser.isNullOrEmpty()) {
                allLeaderboards.forEach { leaderboardEntry ->
                    if (leaderboardEntry.username.lowercase()
                            .contains(leaderboardEntryUser)
                    ) filteredLeaderboards.add(leaderboardEntry)
                }
            }
        }
        filteredLeaderboards.sortByDescending { it.personalRanking.totalPoints }
    }

    fun updateLeaderboardEntry(leaderboardEntry: LeaderboardEntry) {
        viewModelScope.launch {
            leaderboardsService.update(leaderboardEntry)
        }
    }
}