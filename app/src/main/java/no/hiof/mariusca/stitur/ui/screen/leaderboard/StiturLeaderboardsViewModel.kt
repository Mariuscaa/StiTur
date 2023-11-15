package no.hiof.mariusca.stitur.ui.screen.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.LeaderboardEntry
import no.hiof.mariusca.stitur.service.storage.LeaderboardsService
import javax.inject.Inject

@HiltViewModel
class StiturLeaderboardsViewModel @Inject constructor(private val leaderboardsService: LeaderboardsService):
    ViewModel(){

    fun deleteLeaderboardEntry(leaderboardEntry: LeaderboardEntry){
        viewModelScope.launch {
            leaderboardsService.delete(leaderboardEntry.uid)
        }
    }

    fun createLeaderboardEntry(leaderboardEntry: LeaderboardEntry){
        viewModelScope.launch{
            leaderboardsService.save(leaderboardEntry)
        }
    }

    // Flow of leaderboard entries to be observed by UI
    val leaderboardEntries: Flow<List<LeaderboardEntry>> = leaderboardsService.leaderboardEntries

    // SEARCH BAR FUNCTIONALITY
    val leaderboards = leaderboardsService.leaderboardEntries
    val filteredLeaderboards = mutableListOf<LeaderboardEntry>()
    val allLeaderboards = mutableListOf<LeaderboardEntry>()

    init {
        viewModelScope.launch {
            allLeaderboards.addAll(leaderboardsService.getLeaderboardUsername(""))
        }
    }

    fun getLeaderboardEntry(leaderboardEntryUser: String){
        viewModelScope.launch {
            filteredLeaderboards.clear()

            allLeaderboards.forEach { leaderboardEntry ->
                if (leaderboardEntry.username.lowercase().contains(leaderboardEntryUser))
                    filteredLeaderboards.add(leaderboardEntry)
            }
        }
    }


}