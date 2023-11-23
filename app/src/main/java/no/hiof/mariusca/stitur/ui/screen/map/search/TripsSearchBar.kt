package no.hiof.mariusca.stitur.ui.screen.map.search

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TripsSearchBar(
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