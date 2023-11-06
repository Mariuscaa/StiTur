package no.hiof.mariusca.stitur.ui.screen.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.google.android.gms.maps.model.LatLng
import no.hiof.mariusca.stitur.model.Coordinate
import no.hiof.mariusca.stitur.model.Trip

@Composable
fun MapButtons(
    isCreateTripMode: MutableState<Boolean>,
    newTripPoints: MutableList<LatLng>,
    viewModel: StiturMapViewModel
) {
    Row(
        horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = {
            //creatingGeoTreasure(halden)

        }) {
            Text("New GeoTreasure")
        }

        Button(modifier = Modifier.alpha(if (isCreateTripMode.value) 1f else 0f), onClick = {
            isCreateTripMode.value = !isCreateTripMode.value
            if (!isCreateTripMode.value) {
                newTripPoints.clear()
            }
        }) {
            Text(if (isCreateTripMode.value) "Cancel" else "")
        }

        Button(onClick = {
            isCreateTripMode.value = !isCreateTripMode.value
            if (!isCreateTripMode.value) {
                val newTrip = Trip(routeName = "New Trip",
                    routeDescription = "Description of the new trip",
                    difficulty = "Medium",
                    coordinates = newTripPoints.map {
                        Coordinate(
                            it.latitude.toString(), it.longitude.toString()
                        )
                    })
                viewModel.createTrip(newTrip)
                newTripPoints.clear()
            }
        }) {
            Text(if (isCreateTripMode.value) "Save trip" else "Create a new trip")
        }
    }
}