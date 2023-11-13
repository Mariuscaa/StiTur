package no.hiof.mariusca.stitur.ui.screen.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.Coordinate
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.model.calculateDistanceMeters

@Composable
fun MapButtons(
    isCreateTripMode: MutableState<Boolean>,
    newTripPoints: MutableList<LatLng>,
    viewModel: StiturMapViewModel,
    newTrip: MutableState<Trip?>
) {
    // Define the action to perform when the geoTreasure icon is clicked
    val geoTreasureIconClicked: () -> Unit = {
        // Perform the desired action when the icon is clicked
        // For example, you can show a dialog or navigate to a different screen.
    }

    /*
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 25.dp)
        ) {
            IconButton(
                onClick = geoTreasureIconClicked,
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.game_icons_locked_chest),
                    contentDescription = "GeoTreasure icon",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

     */

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 16.dp)
        ) {

            IconButton(
                onClick = geoTreasureIconClicked,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.new_geotreasure2),
                    contentDescription = "GeoTreasure icon",
                    modifier = Modifier.size(48.dp)
                )
            }
            if (!isCreateTripMode.value) {
                IconButton(
                    onClick = {
                        isCreateTripMode.value = !isCreateTripMode.value

                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.create_new_trip),
                        contentDescription = "Hiking icon",
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier
                            .alpha(if (isCreateTripMode.value) 1f else 0f)
                            .padding(end = 2.dp),
                        onClick = {
                            isCreateTripMode.value = !isCreateTripMode.value
                            if (!isCreateTripMode.value) {
                                newTripPoints.clear()
                            }
                        }
                    ) {
                        Text(if (isCreateTripMode.value) "Cancel" else "")
                    }

                    Button(onClick = {
                        isCreateTripMode.value = !isCreateTripMode.value
                        if (!isCreateTripMode.value) {
                            val coordinates = mutableListOf<Coordinate>()
                            for (point in newTripPoints) {
                                val coordinate = Coordinate(
                                    point.latitude.toString(),
                                    point.longitude.toString()
                                )
                                coordinates.add(coordinate)
                            }
                            val distance = calculateDistanceMeters(coordinates)
                            newTrip.value = Trip(
                                lengthInMeters = distance.toLong(),
                                coordinates = newTripPoints.map {
                                    Coordinate(
                                        it.latitude.toString(), it.longitude.toString()
                                    )
                                }
                            )

                        }
                    }) {
                        Text("Save trip")
                    }
                }
            }

        }


    }
}



