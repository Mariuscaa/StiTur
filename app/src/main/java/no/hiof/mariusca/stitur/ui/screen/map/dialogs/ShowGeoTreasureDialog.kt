package no.hiof.mariusca.stitur.ui.screen.map.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.ui.screen.GeoTreasureViewModel

@Composable
fun ShowGeoTreasureDialog(
    selectedGeoTreasure: MutableState<GeoTreasure?>,
    geoTreasureViewModel: GeoTreasureViewModel,
) {
    Dialog(
        onDismissRequest = {
            selectedGeoTreasure.value = null
        },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(onClick = {
                    selectedGeoTreasure.value = null
                }) {
                    Text(text = "Close")
                }
            }
            Column(modifier = Modifier.padding(20.dp)) {
                selectedGeoTreasure.value?.let { geoTreasure ->
                    Text(
                        text = geoTreasure.title,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(fontSize = 28.sp)
                    )
                }

                selectedGeoTreasure.value?.let { geoTreasure ->
                    Text(text = geoTreasure.textContent)
                }

                selectedGeoTreasure.value?.let { geoTreasure ->
                    Text(text = geoTreasure.pictureUrl)
                }

                Button(onClick = {
                    selectedGeoTreasure.value?.let { geoTreasureViewModel.deleteTreasure(it) }
                    selectedGeoTreasure.value = null
                }) {
                    Text(text = "Delete")
                }
            }
        }
    }
}