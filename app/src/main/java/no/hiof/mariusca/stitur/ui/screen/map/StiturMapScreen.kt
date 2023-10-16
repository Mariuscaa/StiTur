package no.hiof.mariusca.stitur.ui.screen.map

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle
import no.hiof.mariusca.stitur.R



@Composable
fun StiturMapScreen(weatherIconClicked: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        StiturMap()


        IconButton(onClick = weatherIconClicked) {

                Image(
                    painter = painterResource(id = R.drawable.ic_weathericon),
                    contentDescription = "Weather icon",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopStart)
                        .padding(10.dp, 10.dp, 0.dp)

                )

        }
    }
}


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun StiturMap() {
    val halden = LatLng(59.1330, 11.3875)
    val cameraPosition = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(halden, 10f)
    }
    var geoJsonLayer by remember { mutableStateOf<GeoJsonLayer?>(null) }
    val context = LocalContext.current
    // val scope = rememberCoroutineScope()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPosition
    ) {
        MapEffect(Unit) { map ->
            if (geoJsonLayer == null) {
                kotlin.runCatching {
                    geoJsonLayer = GeoJsonLayer(map, R.raw.geo_json_sample, context)
                }
                geoJsonLayer?.apply {
                    addLayerToMap()
                    for (feature in features) {
                        when (feature.geometry.geometryType) {
                            /*
                            "Point" -> {
                                val pointStyle = GeoJsonPointStyle()
                                pointStyle.title = feature.getProperty("title")
                                feature.pointStyle = pointStyle
                            }

                            "Polygon" -> {
                                val polygonStyle = GeoJsonPolygonStyle()
                                polygonStyle.strokeColor =
                                    feature.getProperty("stroke").toColorInt()
                                polygonStyle.strokeWidth =
                                    feature.getProperty("stroke-width").toFloat()
                                polygonStyle.fillColor =
                                    feature.getProperty("fill").toColorInt()
                                feature.polygonStyle = polygonStyle
                            }*/

                            "LineString" -> {
                                val lineStringStyle = GeoJsonLineStringStyle()
                                lineStringStyle.color =
                                    "#ff0000".toColorInt()
                                lineStringStyle.width =
                                    20f
                                feature.lineStringStyle = lineStringStyle
                            }
                        }
                    }
                    setOnFeatureClickListener {
                        Toast.makeText(
                            context,
                            "Feature Click ${it.geometry.geometryType}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }


        }
        Marker(MarkerState(position = halden), title = "Halden", snippet = "Marker in Halden.")
    }
}


