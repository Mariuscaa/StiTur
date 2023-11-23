package no.hiof.mariusca.stitur.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun EditUsernameView(
    currentUsername: String,
    newUsername: MutableState<String>,
    userId: String,
    viewModel: ProfileViewModel,
    onUsernameUpdated: () -> Unit
) {
    Column {
        Text("Current Username: $currentUsername", style = MaterialTheme.typography.bodyMedium)
        TextField(value = newUsername.value,
            onValueChange = { newUsername.value = it },
            label = { Text("New Username") })
        Button(onClick = {
            viewModel.updateUsername(userId, newUsername.value)
            onUsernameUpdated()
        }) {
            Text("Update Username")
        }
    }
}