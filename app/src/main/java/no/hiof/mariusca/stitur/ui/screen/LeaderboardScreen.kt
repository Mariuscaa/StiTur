package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.leaderboards.LeaderboardsDummyData
import no.hiof.mariusca.stitur.model.Profile

@Composable
fun LeaderboardScreen() {

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
        SearchBar()
        Spacer(modifier = Modifier.height(40.dp))
        //DummyDataList()

        val leaderboardsDummyData = LeaderboardsDummyData()
        val profiles = leaderboardsDummyData.createDummyProfiles()
        DummyDataList(profiles = profiles)
    }
}

@Composable
fun DummyDataList(profiles: List<Profile>){

    //val users = listOf("Sindre", "Shvan", "Jon", "Marius")

    LazyColumn{
        items(profiles){profile ->
            LeaderboardUserCard(
                userName = profile.username,
                userTotalPoints = profile.personalRanking.totalPoints


            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(){
    var text by remember { mutableStateOf("")}

    TextField(
        value = text,
        onValueChange = {text = it},
        label = {Text("Search for profiles!")},
        leadingIcon = {Icon(Icons.Filled.Search, contentDescription = null)},
        modifier = Modifier.width(250.dp)
    )
}

@Composable
fun LeaderboardUserCard(userName: String, userTotalPoints: Int){
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

        Text(text = userName, //"Sindre",
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

@Preview
@Composable
fun PreviewLeaderboardScreen() {
    LeaderboardScreen()
}