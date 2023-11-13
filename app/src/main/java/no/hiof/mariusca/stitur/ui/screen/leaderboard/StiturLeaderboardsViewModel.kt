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

    fun createLeaderboardEntry(leaderboardEntry: LeaderboardEntry){
        viewModelScope.launch{
            leaderboardsService.save(leaderboardEntry)
        }
    }

    fun deleteLeaderboardEntry(leaderboardEntry: LeaderboardEntry){
        viewModelScope.launch {
            leaderboardsService.delete(leaderboardEntry.uid)
        }
    }

    // Flow of leaderboard entries to be observed by UI
    val leaderboardEntries: Flow<List<LeaderboardEntry>> = leaderboardsService.leaderboardEntries
}
