package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LeaderboardScreen() {
    Text(text = "Leaderboard",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center)
}