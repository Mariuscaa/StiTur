package no.hiof.mariusca.stitur.ui.screen.map.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import no.hiof.mariusca.stitur.model.Trip
import no.hiof.mariusca.stitur.ui.screen.map.TripViewModel

@Composable
fun searchResult(
    textState: MutableState<TextFieldValue>,
    isSearchActive: Boolean,
    viewModel: TripViewModel,
    filteredTrips: MutableList<Trip>,
    selectedTripState: MutableState<Trip?>
): Boolean {
    var isSearchActive1 = isSearchActive
    val searchedText = textState.value.text.lowercase()

    if (searchedText.isNotBlank() && isSearchActive1) {

        viewModel.getFilteredTrips(searchedText)
        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .background(color = Color.White)
        ) {
            items(items = filteredTrips, key = { it.uid }) { item ->
                ColumnItem(item = item.routeName, onItemClick = {
                    selectedTripState.value = item
                    textState.value = TextFieldValue("")
                    isSearchActive1 = false

                })
            }
        }
    }
    return isSearchActive1
}