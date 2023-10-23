package no.hiof.mariusca.stitur.ui.screen.map

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.tripsToGeoJSONFeatureCollection
import org.json.JSONObject
import java.io.ByteArrayInputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Search for trailwalks!") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        modifier = Modifier
            .width(250.dp)
            .height(50.dp)

    )
}

@Composable
fun StiturMapScreen(weatherIconClicked: () -> Unit) {
    var showLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        StiturMap()


        /*IconButton(onClick = weatherIconClicked) {

                Image(
                    painter = painterResource(id = R.drawable.ic_weathericon),
                    contentDescription = "Weather icon",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopStart)
                        .padding(10.dp, 0.dp, 0.dp)
                )
        }*/
    }


    /* Box(modifier = Modifier
         .fillMaxSize()
         .padding(0.dp, 50.dp, 0.dp),
         contentAlignment = Alignment.TopCenter

     ){

         SearchBar()
         }*/

    //TripList()

}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripList(
    modifier: Modifier = Modifier,
    viewModel: StiturMapViewModel = hiltViewModel(),
) {
    val trips = viewModel.trips.collectAsStateWithLifecycle(emptyList())

    Column {
        //TextField(value = movieTitle.value, onValueChange = { movieTitle.value = it })

        LazyVerticalGrid(columns = GridCells.FixedSize(180.dp), content = {
            items(trips.value) { trip ->
                Text(text = trip.routeName)
            }
        }, modifier = modifier.padding(16.dp))
    }
}*/

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun StiturMap(
    viewModel: StiturMapViewModel = hiltViewModel(),
) {
    val geoJsonData = remember { mutableStateOf<String?>(null) }
    val trips by viewModel.trips.collectAsStateWithLifecycle(emptyList())

    val halden = LatLng(59.1330, 11.3875)
    val cameraPosition = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(halden, 10f)
    }
    val context = LocalContext.current
    val gson = Gson()

    // Mutable state variable to store the geoJsonObject
    val geoJsonObjectState = remember { mutableStateOf<JSONObject?>(null) }

    Column {
        Text(text = geoJsonData.value ?: "Loading...")

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition,
        ) {
            trips.forEach { trip ->
                if (trip.coordinates.isNotEmpty()) {
                    trip.coordinates.forEach { coordinate ->
                        val specimenPosition =
                            LatLng(coordinate.lat.toDouble(), coordinate.long.toDouble())
                        Marker(
                            MarkerState(position = specimenPosition),
                            title = trip.routeName,
                            snippet = trip.routeDescription
                        )
                    }

                }
            }


            MapEffect(Unit) { map ->
                if (geoJsonData.value != null) {
                    // Create the geoJsonObject
                    val geoJsonObject = JSONObject(geoJsonData.value!!)
                    geoJsonObjectState.value = geoJsonObject

                    // Check if geoJsonObjectState has a value
                    val geoJsonObjectValue = geoJsonObjectState.value

                    if (geoJsonObjectValue != null) {
                        // Create the GeoJsonLayer
                        val geoJsonLayer = GeoJsonLayer(map, geoJsonObjectValue)

                        geoJsonLayer.apply {
                            addLayerToMap()
                            for (feature in features) {
                                when (feature.geometry.geometryType) {
                                    "LineString" -> {
                                        val lineStringStyle = GeoJsonLineStringStyle()
                                        lineStringStyle.color = "#ff0000".toColorInt()
                                        lineStringStyle.width = 20f
                                        feature.lineStringStyle = lineStringStyle
                                    }
                                }
                            }
                        }

                        // Set an on-feature click listener
                        geoJsonLayer.setOnFeatureClickListener {
                            Toast.makeText(
                                context,
                                "Feature Click ${it.geometry.geometryType}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            // Trigger loading of the GeoJSON data
            geoJsonData.value = gson.toJson(tripsToGeoJSONFeatureCollection(trips))

            Marker(
                MarkerState(position = halden),
                title = "Halden",
                snippet = "Marker in Halden."
            )
        }
    }
}


