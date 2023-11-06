package no.hiof.mariusca.stitur.ui.screen.map

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import no.hiof.mariusca.stitur.R
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

        CreateAllTrips(trips, ongoingTripState, cameraPosition, selectedTripState, context)

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

// From https://www.boltuix.com/2022/11/add-custom-marker-to-google-maps-in.html
fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

@Composable
private fun CreateAllTrips(
    trips: List<Trip>,
    ongoingTripState: MutableState<Trip?>,
    cameraPosition: CameraPositionState,
    selectedTripState: MutableState<Trip?>,
    context: Context
) {
    val startIcon = bitmapDescriptorFromVector(
        context, R.drawable.start
    )

    val endIcon = bitmapDescriptorFromVector(
        context, R.drawable.finish
    )

    trips.forEach { trip ->
        val polylinePoints = mutableListOf<LatLng>()
        if (trip.coordinates.isNotEmpty()) {
            trip.coordinates.forEach { coordinate ->
                val markerPosition =
                    LatLng(coordinate.lat.toDouble(), coordinate.long.toDouble())
                polylinePoints.add(markerPosition)
            }
            Marker(
                MarkerState(position = polylinePoints.toList()[0]),
                title = "Start",
                icon = startIcon,
                anchor = Offset(0.5f, 0.5f),
                onClick = {
                    selectedTripState.value = trip
                    true
                }
            )
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
            Marker(
                MarkerState(position = polylinePoints.toList().last()),
                title = "End",
                icon = endIcon,
                onClick = {
                    selectedTripState.value = trip
                    true
                }
            )
        }
    }
}