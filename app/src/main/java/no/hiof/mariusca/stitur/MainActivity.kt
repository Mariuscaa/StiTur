package no.hiof.mariusca.stitur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.mariusca.stitur.ui.theme.StiTurTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StiTurTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(text = "test")
                    TestMap()

                }
            }
        }
    }
}

@Composable
fun TestMap() {
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StiTurTheme {

    }
}