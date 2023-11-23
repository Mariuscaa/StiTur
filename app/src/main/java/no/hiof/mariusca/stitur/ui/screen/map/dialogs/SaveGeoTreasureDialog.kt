package no.hiof.mariusca.stitur.ui.screen.map.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import no.hiof.mariusca.stitur.model.GeoLocation
import no.hiof.mariusca.stitur.model.GeoTreasure
import no.hiof.mariusca.stitur.model.MinimalProfile
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.map.GeoTreasureViewModel
import no.hiof.mariusca.stitur.ui.screen.ProfileViewModel

@Composable
fun SaveGeoTreasureDialog(
    openDialog: MutableState<Boolean>,
    newGeoTreasure: MutableState<GeoTreasure?>,
    geoTreasureViewModel: GeoTreasureViewModel,
    currentLocation: MutableState<LatLng?>,
    locationRequest: MutableState<LocationRequest?>,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    profileViewModel.getUserInfo(signUpViewModel.currentLoggedInUserId)
    val loggedInProfile = profileViewModel.filteredUser

    val geoLocation = GeoLocation(
        latitude = currentLocation.value?.latitude.toString(),
        longitude = currentLocation.value?.longitude.toString()
    )
    val miniProfile = MinimalProfile(loggedInProfile.value.userID, loggedInProfile.value.username)
    newGeoTreasure.value =
        newGeoTreasure.value?.copy(geoLocation = geoLocation, madeBy = miniProfile)

    Dialog(
        onDismissRequest = {
            openDialog.value = false
            locationRequest.value = null
        },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, top = 50.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Save your GeoTreasure",
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.CenterHorizontally),
                    style = TextStyle(fontSize = 28.sp)
                )
                newGeoTreasure.value?.let { geoTreasure ->
                    OutlinedTextField(
                        value = geoTreasure.title,
                        onValueChange = { newGeoTreasureTitle ->
                            newGeoTreasure.value = geoTreasure.copy(title = newGeoTreasureTitle)
                        },
                        label = { Text("GeoTreasue Title") },
                        modifier = Modifier.padding(15.dp),
                    )
                }
                newGeoTreasure.value?.let { geoTreasure ->
                    OutlinedTextField(
                        value = geoTreasure.textContent,
                        onValueChange = { newGeoTreasureTextContent ->
                            newGeoTreasure.value =
                                geoTreasure.copy(textContent = newGeoTreasureTextContent)
                        },
                        label = { Text("Message") },
                        minLines = 2,
                        maxLines = 2,
                        modifier = Modifier.padding(15.dp),
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp, bottom = 30.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            openDialog.value = false
                            locationRequest.value = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        locationRequest.value = null
                        newGeoTreasure.value?.let { geoTreasureViewModel.createTreasure(it) }
                        newGeoTreasure.value = null
                        openDialog.value = false
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}