package no.hiof.mariusca.stitur.ui.screen.map

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import no.hiof.mariusca.stitur.model.Trip

@Composable
fun MapContent(
    trips: List<Trip>,
    selectedTripState: MutableState<Trip?>,
    ongoingTripState: MutableState<Trip?>,
    cameraPosition: CameraPositionState,
    newTripPoints: MutableList<LatLng>,
    context: Context,
    isCreateTripMode: MutableState<Boolean>,
) {

    val halden = LatLng(59.1330, 11.3875)

    GoogleMap(modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true),
        cameraPositionState = cameraPosition,
        onMapClick = { point ->
            if (isCreateTripMode.value) {
                newTripPoints.add(point)
            }
        }) {

        CreateAllTrips(trips, ongoingTripState, cameraPosition, selectedTripState)

        Marker(
            MarkerState(position = halden), title = "Halden", snippet = "Marker in Halden."
        )

        CreateNewTrip(newTripPoints, context)
    }
}

@Composable
private fun CreateNewTrip(
    newTripPoints: MutableList<LatLng>,
    context: Context
) {
    if (newTripPoints.isNotEmpty()) {
        Polyline(points = newTripPoints.toList(),
            clickable = true,
            color = Color.Red,
            visible = true,
            width = 20.0f,
            onClick = {

                Toast.makeText(context, "This trip is not saved yet.", Toast.LENGTH_LONG)
                    .show()
            })
    }
}

@Composable
private fun CreateAllTrips(
    trips: List<Trip>,
    ongoingTripState: MutableState<Trip?>,
    cameraPosition: CameraPositionState,
    selectedTripState: MutableState<Trip?>
) {
    trips.forEach { trip ->
        val polylinePoints = mutableListOf<LatLng>()
        if (trip.coordinates.isNotEmpty()) {
            trip.coordinates.forEach { coordinate ->
                val markerPosition =
                    LatLng(coordinate.lat.toDouble(), coordinate.long.toDouble())
                polylinePoints.add(markerPosition)
            }
            Polyline(points = polylinePoints.toList(),
                startCap = RoundCap(),
                endCap = RoundCap(),
                jointType = JointType.ROUND,
                clickable = true,
                color = if (trip == ongoingTripState.value) {
                    Color(0xFF006600)
                } else {
                    Color(0xFF000099)
                },
                width = 200.0f / cameraPosition.position.zoom,
                onClick = {
                    selectedTripState.value = trip
                })
        }
    }
}