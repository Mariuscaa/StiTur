package no.hiof.mariusca.stitur.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.signup.SignUpViewModel

data class GeoLocation (
    val geoLocationID: String = "",
    val longitude: String = "",
    val latitude: String = ""
)
//viewModel.createTreasure(newTreasure)


@Composable
fun GeoTreasureScreen(signupviewModel: SignUpViewModel = hiltViewModel(), geotreasureviewModel: GeoTreasureViewModel = hiltViewModel()) {
    val user by signupviewModel.currentLoggedInUser.collectAsState(initial = Profile())
    val GeoTreasures: MutableList<GeoTreasure> = mutableListOf()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GeoTreasure",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        val dummyGeoLocation = GeoLocation("2f", "11.353666857294389", "59.12283718051173")
        /*val dummyTreasure = GeoTreasure("1e", "SommerTur", "En fin tur i sommer", "en url fra firebase kanskje", dummyGeoLocation)
        GeoTreasures.add(dummyTreasure)
        geotreasureviewModel.createTreasure(dummyTreasure)
        ShowGeoTreasure(dummyTreasure)*/
    }
}
@Composable
fun ShowGeoTreasure(geoTreasure: GeoTreasure){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(all = 6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Image(
            //Den drawablen under skal endres til å bli noe annet
            painter = painterResource(id = R.drawable.vector),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 10.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        ) {
            Text(
                text = geoTreasure.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = geoTreasure.textContent)
        }

        Image(
            painter = painterResource(id = R.drawable.group_icon_score),
            contentDescription = null
        )
    }
}


fun createDummyGeoLocation(): Any {

    return GeoLocation("2f", "11.4050194", "59.1342646")
}

fun createDummyTreasure(geoLocation: no.hiof.mariusca.stitur.model.GeoLocation): GeoTreasure {

    return GeoTreasure("1e", "SommerTur", "En fin tur i sommer", "en url fra firebase kanskje", geoLocation)
}
/*
@Preview
@Composable
fun PreviewShowGeoTreasure() {
    val dummyGeoLocation = GeoLocation("2f", "11.4050194", "59.1342646")
    val dummyTreasure = GeoTreasure("1e", "SommerTur", "En fin tur i sommer", "en url fra firebase kanskje", dummyGeoLocation)
    ShowGeoTreasure(dummyTreasure)
}

 */