package no.hiof.mariusca.stitur.ui.screen.map.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.LatLng
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.ui.screen.map.StiturMapViewModel

@Composable
fun SaveTripDialog(
    openDialog: MutableState<Boolean>,
    newTrip: MutableState<Trip?>,
    stiturMapViewModel: StiturMapViewModel,
    newTripPoints: MutableList<LatLng>,
    isCreateTripMode: MutableState<Boolean>,
) {
    Dialog(
        onDismissRequest = {
            openDialog.value = false
        },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {

                Text(
                    text = "Save your trip",
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.CenterHorizontally),
                    style = TextStyle(fontSize = 28.sp)
                )
                newTrip.value?.let { trip ->
                    OutlinedTextField(
                        value = trip.routeName,
                        onValueChange = { newRouteName ->
                            newTrip.value = trip.copy(routeName = newRouteName)
                        },
                        label = { Text("Name") },
                        modifier = Modifier.padding(15.dp),
                        isError = trip.routeName.isEmpty()
                    )
                }
                newTrip.value?.let { trip ->
                    OutlinedTextField(
                        value = trip.routeDescription,
                        onValueChange = { newRouteDescription ->
                            newTrip.value = trip.copy(routeDescription = newRouteDescription)
                        },
                        minLines = 2,
                        maxLines = 2,
                        label = { Text("Description") },
                        modifier = Modifier.padding(15.dp)
                    )
                }
                newTrip.value?.let { trip ->
                    OutlinedTextField(
                        value = trip.difficulty,
                        onValueChange = { newDifficulty ->
                            newTrip.value = trip.copy(difficulty = newDifficulty)
                        }, label = { Text("Difficulty") },
                        modifier = Modifier.padding(15.dp)
                    )
                }

                newTrip.value?.let { trip ->
                    OutlinedTextField(
                        value = trip.lengthInMeters.toString(),
                        onValueChange = {},
                        label = { Text("Estimated length in meters.") },
                        modifier = Modifier.padding(15.dp),
                        enabled = false
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            openDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        newTrip.value?.let { stiturMapViewModel.createTrip(it) }
                        newTripPoints.clear()
                        openDialog.value = false
                        isCreateTripMode.value = !isCreateTripMode.value
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}