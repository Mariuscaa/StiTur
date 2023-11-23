package no.hiof.mariusca.stitur.ui.screen.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.Coordinate
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.model.calculateDistanceMeters

@Composable
fun MapButtons(
    isCreateTripMode: MutableState<Boolean>,
    newTripPoints: MutableList<LatLng>,
    newTrip: MutableState<Trip?>,
    openNewTripDialog: MutableState<Boolean>,
    ongoingTripState: MutableState<Trip?>,
    selectedTripState: MutableState<Trip?>,
    cameraFollowingGps: MutableState<Boolean>,
    openNewGeoTreasureDialog: MutableState<Boolean>,
    newGeoTreasure: MutableState<GeoTreasure?>,
) {

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
            if (ongoingTripState.value != null) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ActiveTripButton(selectedTripState, ongoingTripState)

                    GpsCameraLockButtons(cameraFollowingGps)
                }
            }

            NewGeoTreasureButton(openNewGeoTreasureDialog, newGeoTreasure)
            CreateNewTripButtons(isCreateTripMode, newTripPoints, newTrip, openNewTripDialog)
        }
    }
}

@Composable
private fun CreateNewTripButtons(
    isCreateTripMode: MutableState<Boolean>,
    newTripPoints: MutableList<LatLng>,
    newTrip: MutableState<Trip?>,
    openNewTripDialog: MutableState<Boolean>
) {
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
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel")
            }

            SaveTripButton(isCreateTripMode, newTripPoints, newTrip, openNewTripDialog)
        }
    }
}

@Composable
private fun SaveTripButton(
    isCreateTripMode: MutableState<Boolean>,
    newTripPoints: MutableList<LatLng>,
    newTrip: MutableState<Trip?>,
    openNewTripDialog: MutableState<Boolean>
) {
    Button(onClick = {
        if (isCreateTripMode.value) {
            val coordinates = mutableListOf<Coordinate>()
            for (point in newTripPoints) {
                val coordinate = Coordinate(
                    point.latitude.toString(),
                    point.longitude.toString()
                )
                coordinates.add(coordinate)
            }
            val distance = calculateDistanceMeters(coordinates)
            if (newTrip.value == null) {
                newTrip.value = Trip(
                    lengthInMeters = distance.toLong(),
                    coordinates = newTripPoints.map {
                        Coordinate(
                            it.latitude.toString(), it.longitude.toString()
                        )
                    }
                )
            } else {
                newTrip.value!!.lengthInMeters = distance.toLong()
                newTrip.value!!.coordinates = newTripPoints.map {
                    Coordinate(
                        it.latitude.toString(), it.longitude.toString()
                    )
                }
            }
        }
        openNewTripDialog.value = true
    }) {
        Text("Save trip")
    }
}

@Composable
private fun NewGeoTreasureButton(
    openNewGeoTreasureDialog: MutableState<Boolean>,
    newGeoTreasure: MutableState<GeoTreasure?>
) {
    IconButton(
        onClick = {
            openNewGeoTreasureDialog.value = true
            newGeoTreasure.value = GeoTreasure()
        },
    ) {
        Image(
            painter = painterResource(id = R.drawable.new_geotreasure2),
            contentDescription = "GeoTreasure icon",
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun GpsCameraLockButtons(cameraFollowingGps: MutableState<Boolean>) {
    if (cameraFollowingGps.value) {
        IconButton(
            onClick = { cameraFollowingGps.value = false },
            modifier = Modifier.padding(end = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.gps_tracking_off),
                contentDescription = "Unlock Camera",
                modifier = Modifier.size(48.dp)
            )
        }
    } else {
        IconButton(
            onClick = { cameraFollowingGps.value = true },
            modifier = Modifier.padding(end = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.gps_tracking),
                contentDescription = "Lock Camera",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun ActiveTripButton(
    selectedTripState: MutableState<Trip?>,
    ongoingTripState: MutableState<Trip?>
) {
    IconButton(
        onClick = { selectedTripState.value = ongoingTripState.value },
    ) {
        Image(
            painter = painterResource(id = R.drawable.active_trips),
            contentDescription = "Active trip",
            modifier = Modifier.size(48.dp)
        )
    }
}



