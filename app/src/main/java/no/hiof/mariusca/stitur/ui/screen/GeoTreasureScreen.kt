package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import no.hiof.mariusca.stitur.model.GeoTreasure

/*
val geoTreasureID: String = "",
val title: String = "",
val textContent: String = "",
val pictureUrl: String = "",
val geoLocation: GeoLocation = GeoLocation()

 */
@Composable
fun GeoTreasureScreen() {
    Text(text = "GeoTreasure",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center)

    CreateDummyTreasure()
}

@Composable
fun ShowGeoTreasure(){

val DummyTreasure = GeoTreasure("1e", "SommerTur", "En fin tur i sommer", "en url fra firebase kanskje")
}

fun CreateDummyTreasure(){

}



@Preview
@Composable
fun PreviewShowGeoTreasure() {
    ShowGeoTreasure()
}