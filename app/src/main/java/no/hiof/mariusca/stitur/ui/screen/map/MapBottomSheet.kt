package no.hiof.mariusca.stitur.ui.screen.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.hiof.mariusca.stitur.model.Trip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapBottomSheet(
    selectedTripState: MutableState<Trip?>,
    sheetState: SheetState,
    showBottomSheet: Boolean,
    toggleBottomSheet: (Boolean) -> Unit,
    scope: CoroutineScope,
    ongoingTripState: MutableState<Trip?>,
    viewModel: StiturMapViewModel
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                toggleBottomSheet(false)
                selectedTripState.value = null
            }, sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(10.dp, top = 0.dp, bottom = 16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                ) {
                    Button(modifier = Modifier.padding(end = 10.dp), onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                toggleBottomSheet(false)
                                selectedTripState.value = null
                            }
                        }
                    }) {
                        Text("Close (X)")
                    }
                }

                DynamicStartAndStopButton(
                    selectedTripState,
                    ongoingTripState,
                    scope,
                    sheetState,
                    toggleBottomSheet
                )

                TripOverview(selectedTripState)

                Button(modifier = Modifier.padding(top = 14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    onClick = {
                        selectedTripState.value?.let { viewModel.deleteTrip(it) }
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                toggleBottomSheet(false)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DynamicStartAndStopButton(
    selectedTripState: MutableState<Trip?>,
    ongoingTripState: MutableState<Trip?>,
    scope: CoroutineScope,
    sheetState: SheetState,
    toggleBottomSheet: (Boolean) -> Unit
) {
    if (selectedTripState.value == ongoingTripState.value) {
        Button(modifier = Modifier.padding(bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006600)),
            onClick = {
                ongoingTripState.value = null
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        toggleBottomSheet(false)
                        selectedTripState.value = null
                    }
                }
            }) {
            Text("End trip")
        }
    } else {
        Button(modifier = Modifier.padding(bottom = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006600)),
            onClick = {
                ongoingTripState.value = selectedTripState.value
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        toggleBottomSheet(false)
                        selectedTripState.value = null
                    }
                }
            }) {
            Text("Start trip")
        }
    }
}

@Composable
private fun TripOverview(selectedTripState: MutableState<Trip?>) {
    selectedTripState.value?.let { selectedTrip ->
        Text("Selected Trip: ${selectedTrip.routeName}")
        Text("Description: ${selectedTrip.routeDescription}")
        Text("Difficulty: ${selectedTrip.difficulty}")
    }
}