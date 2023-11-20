package no.hiof.mariusca.stitur.ui.screen.leaderboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.LeaderboardEntry
import no.hiof.mariusca.stitur.model.Tiers

@Composable
fun LeaderboardScreen(viewModel: StiturLeaderboardsViewModel) {

    // State - Search bar:
    var searchQuery by remember{
        mutableStateOf("")
    }

    var tierState by remember{
        mutableStateOf(Tiers.ALL)
    }

    LaunchedEffect(searchQuery, tierState){
        viewModel.getLeaderboardEntry(searchQuery.lowercase(), tierState)
    }

    val filteredEntries = viewModel.filteredLeaderboards

    // Collect flow of leaderboard entries and determine which list to display based on if query has content
    val leaderboardEntries by viewModel.leaderboardEntries.collectAsState(initial = listOf())

    // Display leaderboard entries sorted by total points in descending order:
    val displayEntries = if(searchQuery.isBlank()) leaderboardEntries.sortedByDescending { it.personalRanking.totalPoints }
    else filteredEntries


    val customBackgroundColor = Color(0xFF133c07)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customBackgroundColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleHeader()
        Spacer(modifier = Modifier.height(40.dp))
        SearchBar(searchQuery){
                newQuery -> searchQuery = newQuery }
        Spacer(modifier = Modifier.height(40.dp))

        LeaderboardEntryDataList(leaderboardEntries = displayEntries)
    }
}

@Composable
fun LeaderboardEntryDataList(leaderboardEntries: List<LeaderboardEntry>){

    LazyColumn(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        items(leaderboardEntries) { leaderboardEntry ->
            LeaderboardUserCard(leaderboardEntry = leaderboardEntry)
        }
    }
}

@Composable
fun TitleHeader(){

    Spacer(modifier = Modifier.height(70.dp))
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Icon(
            Icons.Filled.List,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .width(35.dp)
                .height(35.dp)
        )

        Text(text = "Leaderboard",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit){
    var text by remember {
        mutableStateOf(query)
    }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        label = {Text("Search for profiles!")},
        leadingIcon = {Icon(Icons.Filled.Search, contentDescription = null)},
        modifier = Modifier.width(250.dp)
    )
}


@Composable
fun LeaderboardUserCard(leaderboardEntry: LeaderboardEntry){

    val userName = leaderboardEntry.username
    val userTotalPoints = leaderboardEntry.personalRanking.totalPoints

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(all = 6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .width(250.dp)
    )
    {
        Image(
            painter = painterResource(id = R.drawable.user_icon_woman),
            contentDescription = null,
            modifier = Modifier.padding(end = 10.dp)
        )

        Text(text = userName,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(end = 10.dp)
        )

        Image( // Fix this: Icon placement in relation to input name. Swap with tier icon?
            painter = painterResource(id = R.drawable.group_icon_score),
            contentDescription = null,
            modifier = Modifier.padding(end = 10.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = "Total points:",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp)
            )

            Text(text = "$userTotalPoints",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}