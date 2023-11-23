package no.hiof.mariusca.stitur.ui.screen.map.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.ui.screen.map.GeoTreasureViewModel

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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(10.dp),
            ) {
                Button(
                    onClick = {
                        selectedGeoTreasure.value = null
                    },
                ) {
                    Text(text = "Close")
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 35.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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
                    Text(
                        text = "Made by: ${geoTreasure.madeBy.userName}",
                        modifier = Modifier
                            .padding(bottom = 20.dp, top = 10.dp)
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(
                            fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray
                        )
                    )
                }

                Button(
                    onClick = {
                        selectedGeoTreasure.value?.let { geoTreasureViewModel.deleteTreasure(it) }
                        selectedGeoTreasure.value = null
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}