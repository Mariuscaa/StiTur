package no.hiof.mariusca.stitur.ui.screen.map

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun StiturMapScreen() {
    val halden = LatLng(59.1330, 11.3875)
    val cameraPosition = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(halden, 10f)
    }
    Text(text = "Maps")
    GoogleMap(
        cameraPositionState = cameraPosition
    ) {
        Marker(MarkerState(position = halden), title = "Halden", snippet = "Marker in Halden.")
    }
}