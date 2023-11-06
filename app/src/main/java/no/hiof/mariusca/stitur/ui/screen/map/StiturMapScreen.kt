package no.hiof.mariusca.stitur.ui.screen.map

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.mariusca.stitur.R
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
    state: MutableState<TextFieldValue>, placeHolder: String, modifier: Modifier
) {
    TextField(value = state.value, onValueChange = { value ->
        state.value = value
    },

        modifier
            .fillMaxWidth()
            .padding(80.dp, 50.dp, 80.dp, 0.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp))
            .height(50.dp), leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon"
            )
        }, placeholder = {
            Text(text = placeHolder)
        }, colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
        ), maxLines = 1, singleLine = true
    )

}


@Composable
fun StiturMapScreen(
    weatherIconClicked: () -> Unit, modifier: Modifier = Modifier,
    viewModel: StiturMapViewModel = hiltViewModel()
) {
    val filteredTrips = viewModel.filteredTrips
    val isCreateTripMode = remember { mutableStateOf(false) }
    val newTripPoints = remember {
        mutableStateListOf<LatLng>()
    }
    val selectedTripState = remember { mutableStateOf<Trip?>(null) }

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
                StiturMap(
                    isCreateTripMode = isCreateTripMode,
                    newTripPoints = newTripPoints,
                    selectedTripState = selectedTripState
                    )

                IconButton(onClick = weatherIconClicked) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_weathericon),
                        contentDescription = "Weather icon",
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopStart)
                    )
                }
                MapButtons(
                    isCreateTripMode = isCreateTripMode,
                    newTripPoints = newTripPoints,
                    viewModel = viewModel
                )
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

                val searchedText = textState.value.text.lowercase()

                if (searchedText.isNotBlank()) {

                    viewModel.getCreatedTrip(searchedText)
                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        items(items = filteredTrips, key = { it.uid }) { item ->
                            ColumnItem(item = item.routeName)
                        }
                    }
                }
            }
        })
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StiturMap(
    viewModel: StiturMapViewModel = hiltViewModel(),
    isCreateTripMode: MutableState<Boolean>,
    newTripPoints: MutableList<LatLng>,
    selectedTripState: MutableState<Trip?>
    ) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val trips by viewModel.trips.collectAsStateWithLifecycle(emptyList())
    val ongoingTripState = remember { mutableStateOf<Trip?>(null) }

    val halden = LatLng(59.1330, 11.3875)
    val cameraPosition = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(halden, 12f)
    }

    val context = LocalContext.current

    val toggleBottomSheet: (Boolean) -> Unit = { showBottomSheet = it }


    LaunchedEffect(selectedTripState.value) {
        if (selectedTripState.value != null) {
            sheetState.show()
            showBottomSheet = true
        }
    }

    Column {
        MapContent(
            trips = trips,
            selectedTripState = selectedTripState,
            ongoingTripState = ongoingTripState,
            cameraPosition = cameraPosition,
            newTripPoints = newTripPoints,
            context = context,
            isCreateTripMode = isCreateTripMode
        )

        MapBottomSheet(
            selectedTripState = selectedTripState,
            sheetState = sheetState,
            showBottomSheet = showBottomSheet,
            toggleBottomSheet = toggleBottomSheet,
            scope = scope,
            ongoingTripState = ongoingTripState,
            viewModel = viewModel
        )
    }
}
