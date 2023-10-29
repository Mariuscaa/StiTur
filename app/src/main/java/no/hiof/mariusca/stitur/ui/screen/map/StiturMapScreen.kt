package no.hiof.mariusca.stitur.ui.screen.map

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.Trip





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(){

    var text by remember { mutableStateOf("")}

    TextField(
        value = text,
        onValueChange = {text = it},
        label = {Text("Search for trailwalks!")},
        leadingIcon = {Icon(Icons.Filled.Search, contentDescription = "Liste med turer")},
        modifier = Modifier
            .width(250.dp)
            .height(50.dp)
    )


}


@Composable
fun StiturMapScreen(weatherIconClicked: () -> Unit) {
    // TODO: Make loading gif / skeleton
    var showLoading by remember { mutableStateOf(true) }

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
                    .padding(10.dp, 0.dp, 0.dp)
            )
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 50.dp, 0.dp),
        contentAlignment = Alignment.TopCenter

    ) {
        SearchBar()
    }

}

@OptIn(MapsComposeExperimentalApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StiturMap(
    viewModel: StiturMapViewModel = hiltViewModel(),
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val trips by viewModel.trips.collectAsStateWithLifecycle(emptyList())
    val selectedTripState = remember { mutableStateOf<Trip?>(null) }

    val halden = LatLng(59.1330, 11.3875)
    val cameraPosition = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(halden, 12f)
    }

    val markerStateList =
        remember { mutableStateListOf(MarkerState(halden)) }
    val context = LocalContext.current
    val isCreateTripMode = remember { mutableStateOf(false)}
    val newTripPoints = remember { mutableStateOf(false)}


    LaunchedEffect(selectedTripState.value) {
        if (selectedTripState.value != null) {
            sheetState.show()
            showBottomSheet = true
        }
    }

    /*  GeoJson attempt
        val context = LocalContext.current
        val gson = Gson()

        val geoJsonObjectState = remember { mutableStateOf<JSONObject?>(null) }*/

    Column {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition,
            onMapClick = {val isPositionExist = markerStateList.any { state -> state.position == it }
                if (!isPositionExist) {
                    markerStateList.add(MarkerState(it))
                }}
        ) {
            val polylinePoints = mutableListOf<LatLng>()

            trips.forEach { trip ->
                if (trip.coordinates.isNotEmpty()) {
                    trip.coordinates.forEach { coordinate ->
                        val markerPosition =
                            LatLng(coordinate.lat.toDouble(), coordinate.long.toDouble())
                        polylinePoints.add(markerPosition)
                    }
                    Polyline(
                        points = polylinePoints,
                        clickable = true,
                        color = Color.Blue,
                        width = 20.0f,
                        onClick = {
                            selectedTripState.value = trip
                        }
                    )
                }
            }

            Marker(
                MarkerState(position = halden),
                title = "Halden",
                snippet = "Marker in Halden."
            )

            Polyline(
                points = markerStateList.map { it.position },
                clickable = true,
                color = Color.Red,
                visible = true,
                width = 20.0f,
                onClick = {
                    Toast.makeText(context, "Clicked polyline", Toast.LENGTH_LONG).show()
                })


            /*MapEffect(Unit) { map ->
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
            geoJsonData.value = gson.toJson(tripsToGeoJSONFeatureCollection(trips))*/
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    selectedTripState.value = null
                },
                sheetState = sheetState
            ) {
                // Sheet content
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            selectedTripState.value = null
                        }
                    }
                }) {
                    Text("Hide bottom sheet")
                }

                selectedTripState.value?.let { selectedTrip ->
                    Text("Selected Trip: ${selectedTrip.routeName}")
                    Text("Description: ${selectedTrip.routeDescription}")
                    Text("Difficulty: ${selectedTrip.difficulty}")

                }

            }
        }


    }
}



