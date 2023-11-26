package no.hiof.mariusca.stitur.ui.screen.leaderboard

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

// Sadly did not get enough time to implement this properly. Can be viewed in design preview, but
// not in app.
@Composable
fun LeaderboardTierScreen() {
    val customBackgroundColor = Color(0xFF133c07)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customBackgroundColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LeaderboardTiersTitleHeader()
        Spacer(modifier = Modifier.height(40.dp))
        LeaderboardTierIcons()
        TestUserList()
    }
}

@Composable
fun LeaderboardTierLists() {
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun LeaderboardTiersTitleHeader() {
    Icon(
        Icons.Filled.List,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .width(35.dp)
            .height(35.dp)
    )

    Text(
        text = "Leaderboard Tiers ", style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold, color = Color.White
        )
    )

    Text(
        text = "Weekly", style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold, color = Color.White
        )
    )
}

@Composable
fun TestUserList() {
    LazyColumn {
        item {
            LeaderboardTierUserCard()
        }
        item {
            DummyUserCard()
        }
        item {
            AnotherDummyUserCard()
        }
    }
}

@Composable
fun LeaderboardTierIcons() {
    var selectedIcon by remember { mutableStateOf<String?>(null) }
    val onIconClick = { iconTitle: String ->
        selectedIcon = if (selectedIcon == iconTitle) null else iconTitle
    }

    Spacer(modifier = Modifier.height(25.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        LeaderboardTierIcon(
            imageResId = R.drawable.leaderboard_tier_silver,
            title = "Silver",
            isSelected = selectedIcon == "Silver",
            onClick = onIconClick
        )
        LeaderboardTierIcon(
            imageResId = R.drawable.leaderboard_tier_gold,
            title = "Gold",
            isSelected = selectedIcon == "Gold",
            onClick = onIconClick
        )
        LeaderboardTierIcon(
            imageResId = R.drawable.leaderboard_tier_diamond,
            title = "Diamond",
            isSelected = selectedIcon == "Diamond",
            onClick = onIconClick
        )
        LeaderboardTierIcon(
            imageResId = R.drawable.leaderboard_tier_platinum,
            title = "Platinum",
            isSelected = selectedIcon == "Platinum",
            onClick = onIconClick
        )
    }
}

@Composable
fun LeaderboardTierIcon(
    @DrawableRes imageResId: Int, title: String, isSelected: Boolean, onClick: (String) -> Unit
) {
    val iconSize = if (isSelected) 60.dp else 45.dp
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick(title) }) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = title,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun LeaderboardTierUserCard() {
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(all = 6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .width(250.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_icon_woman),
            contentDescription = null,
            modifier = Modifier.padding(end = 10.dp)
        )

        Text(
            text = "Test User", //Insert user name here
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(end = 10.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.leaderboard_tier_gold),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(25.dp, 25.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Weekly score:",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp)
            )

            val dummyValue = 2560
            Text(
                text = "$dummyValue Points", //Insert personalRanking.weeklyPoints here
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun DummyUserCard() {
    Spacer(modifier = Modifier.height(20.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(all = 6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .width(250.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_icon_woman),
            contentDescription = null,
            modifier = Modifier.padding(end = 10.dp)
        )

        Text(
            text = "Test User 2", //Insert user name here
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(end = 10.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.leaderboard_tier_gold),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(25.dp, 25.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Weekly score:",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp)
            )

            val dummyValue = 2900
            Text(
                text = "$dummyValue Points", //Insert personalRanking.weeklyPoints here
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun AnotherDummyUserCard() {
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(all = 6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .width(250.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_icon_woman),
            contentDescription = null,
            modifier = Modifier.padding(end = 10.dp)
        )

        Text(
            text = "Test User 3", //Insert user name here
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(end = 10.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.leaderboard_tier_gold),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(25.dp, 25.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Weekly score:",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp)
            )

            val dummyValue = 2950
            Text(
                text = "$dummyValue Points", //Insert personalRanking.weeklyPoints here
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
fun PreviewLeaderboardsTierScreen() {
    LeaderboardTierScreen()
}
