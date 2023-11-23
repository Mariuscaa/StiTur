package no.hiof.mariusca.stitur.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.GeoTreasure

@Composable
fun GeoTreasureCard(geoTreasureObj: GeoTreasure) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .heightIn(max = 150.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.game_icons_locked_chest),
                contentDescription = "Treasure",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = geoTreasureObj.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    IconTextCombo(icon = Icons.Default.Face, text = geoTreasureObj.madeBy.userName)
                    IconTextCombo(
                        icon = Icons.Default.Email, text = "Melding: ${geoTreasureObj.textContent}"
                    )
                }
            }
        }
    }
}