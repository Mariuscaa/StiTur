package no.hiof.mariusca.stitur.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.TripHistory

@Composable
fun TripHistoryCard(tripHistory: TripHistory) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.maps),
                contentDescription = "Map Thumbnail",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = tripHistory.trackedTrip.routeName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Points",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = "${tripHistory.pointsEarned}")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Distance",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = "${tripHistory.trackedDistanceKm} km")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Duration",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = "${tripHistory.durationMinutes} min")
                }
            }
        }
    }
}