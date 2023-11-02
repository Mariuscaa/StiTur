package no.hiof.mariusca.stitur.ui.screen.map

import android.Manifest
import android.graphics.Color.rgb
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Cap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
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
        onValueChange = { value ->
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
    //list: List<String>,
    viewModel: StiturMapViewModel = hiltViewModel()
) {
    val filteredTrips = viewModel.filteredTrips

    //viewModel: StiturMapViewModel = hiltViewModel()

    //val activeTrips by viewModel.trips.collectAsStateWithLifecycle(emptyList())
    //val selectedTripState = remember { mutableStateOf<Trip?>(null) }


    // Inspired by https://github.com/android/platform-samples/blob/main/samples/base/src/main/java/com/example/platform/base/PermissionBox.kt
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first()),
        onGranted = {
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
                SearchView(
                    state = textState,
                    placeHolder = "Search for trailwalks!",
                    modifier = modifier
                )

                val searchedText = textState.value.text

                if (searchedText.isNotBlank()) {
                    viewModel.getCreatedTrip(searchedText)
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        items(items = filteredTrips, key = { it.uid }) { item ->
                            ColumnItem(item = item.routeName)
                        }
                    }
                }
            }

        }
    }
/*
@Composable
fun creatingGeoTreasure(pos){
val customMarkerImage = painterResource(id = R.drawable.your_custom_image) // Replace with your image resource ID
Marker(
MarkerState(position),
title = "Halden",
snippet = "Marker in Halden.",
icon = customMarkerImage, // Set the custom image here
onClick = {
    // Handle the click event for the marker
    Toast.makeText(context, "Custom marker clicked!", Toast.LENGTH_LONG).show()
}
)
}
Marker(
MarkerState(position = halden), title = "Halden", snippet = "Marker in Halden."
)
*/


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
    val ongoingTripState = remember { mutableStateOf<Trip?>(null) }

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

    Column {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                //creatingGeoTreasure(halden)

            }) {
                Text("New GeoTreasure")
            }

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

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = true),
            cameraPositionState = cameraPosition,
            onMapClick = { point ->
                if (isCreateTripMode.value) {
                    newTripPoints.add(point)
                }
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

                        Toast.makeText(context, "This trip is not saved yet.", Toast.LENGTH_LONG)
                            .show()
                    })
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    selectedTripState.value = null
                }, sheetState = sheetState
            ) {
                Column(modifier = Modifier.padding(10.dp, top = 0.dp, bottom = 16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier.padding(end = 10.dp),
                            onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                        selectedTripState.value = null
                                    }
                                }
                            }) {
                            Text("Close (X)")
                        }
                    }

                    if (selectedTripState.value == ongoingTripState.value) {
                        Button(
                            modifier = Modifier.padding(bottom = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006600)),
                            onClick = {
                                ongoingTripState.value = null
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                        selectedTripState.value = null
                                    }
                                }
                            }) {
                            Text("End trip")
                        }
                    } else {
                        Button(
                            modifier = Modifier.padding(bottom = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006600)),
                            onClick = {
                                ongoingTripState.value = selectedTripState.value
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                        selectedTripState.value = null
                                    }
                                }
                            }) {
                            Text("Start trip")
                        }
                    }

                    selectedTripState.value?.let { selectedTrip ->
                        Text("Selected Trip: ${selectedTrip.routeName}")
                        Text("Description: ${selectedTrip.routeDescription}")
                        Text("Difficulty: ${selectedTrip.difficulty}")
                    }

                    Button(
                        modifier = Modifier.padding(top = 14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            selectedTripState.value?.let { viewModel.deleteTrip(it) }
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                    selectedTripState.value = null
                                }
                            }
                        }) {
                        Text("Delete trip")
                    }
                }

            }
        }
    }
}
