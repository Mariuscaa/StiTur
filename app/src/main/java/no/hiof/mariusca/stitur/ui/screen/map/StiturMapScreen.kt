package no.hiof.mariusca.stitur.ui.screen.map

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import no.hiof.mariusca.stitur.model.Coordinate
import no.hiof.mariusca.stitur.model.Trip


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {

    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Search for trailwalks!") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Liste med turer") },
        modifier = Modifier
            .width(250.dp)
            .height(50.dp)
    )


}

 */

@Composable
fun ColumnItem(item: String) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = item, modifier = Modifier.padding(vertical = 10.dp), fontSize = 22.sp)
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String,
    modifier: Modifier
) {
    TextField(
        value = state.value,
        onValueChange = {value->
            state.value = value
        },

        modifier
            .fillMaxWidth()
            .padding(80.dp, 50.dp, 80.dp, 0.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp))
            .height(50.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        placeholder = {
            Text(text = placeHolder)
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White

        ),
        maxLines = 1,
        singleLine = true
    )

}


@Composable
fun StiturMapScreen(
    weatherIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
    list: List<String> ) {

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
                    .size(100.dp)
                    .align(Alignment.TopStart)
            )
        }
    }
        Column(modifier.fillMaxSize()) {

            val textState = remember {
                mutableStateOf(TextFieldValue(""))
            }
            SearchView(state = textState, placeHolder = "Search for trailwalks!", modifier = modifier)

            val searchedText = textState.value.text

            if(searchedText.isNotBlank()) {

                LazyColumn(modifier = Modifier.padding(10.dp)) {
                    items(items = list.filter {
                        it.contains(searchedText, ignoreCase = true)
                    }, key = { it }) { item ->
                        ColumnItem(item = item)


                    }
                }
            }
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


    val context = LocalContext.current
    val isCreateTripMode = remember { mutableStateOf(false) }
    val newTripPoints = remember {
        mutableStateListOf<LatLng>()
    }


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
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.alpha(if (isCreateTripMode.value) 1f else 0f),
                onClick = {
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
                    // Save the drawing as a trip when exiting drawing mode
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

        GoogleMap(modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition,
            onMapClick = { point ->
                if (isCreateTripMode.value) {
                    newTripPoints.add(point)
                }
                Log.v("EH", newTripPoints.toString())
            }) {


            trips.forEach { trip ->
                val polylinePoints = mutableListOf<LatLng>()
                if (trip.coordinates.isNotEmpty()) {
                    trip.coordinates.forEach { coordinate ->
                        val markerPosition =
                            LatLng(coordinate.lat.toDouble(), coordinate.long.toDouble())
                        polylinePoints.add(markerPosition)
                    }
                    Polyline(points = polylinePoints.toList(),
                        clickable = true,
                        color = Color.Blue,
                        width = 20.0f,
                        onClick = {
                            selectedTripState.value = trip
                        })
                }
            }

            Marker(
                MarkerState(position = halden), title = "Halden", snippet = "Marker in Halden."
            )

            if (newTripPoints.isNotEmpty()) {
                Polyline(
                    points = newTripPoints.toList(),
                    clickable = true,
                    color = Color.Red,
                    visible = true,
                    width = 20.0f,
                    onClick = {
                        Toast.makeText(context, "Clicked polyline", Toast.LENGTH_LONG).show()
                    })
            }

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
                }, sheetState = sheetState
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
