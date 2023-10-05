package no.hiof.mariusca.stitur.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.hiof.mariusca.stitur.ui.screen.SettingsScreen
import no.hiof.mariusca.stitur.ui.screen.TempStartPage
import no.hiof.mariusca.stitur.ui.screen.map.StiturMapScreen

@Composable
fun HomeScreen() {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Navigation page")
        NavigationApp()
    }
}

enum class Screen {
    StiturMap, Settings, TempStartPage
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = { Text(text = "Title") },
            actions = {
                IconButton(onClick = { navController.navigate(Screen.TempStartPage.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home button"
                    )
                }
                IconButton(onClick = { navController.navigate(Screen.StiturMap.name) }) {
                    Icon(
                        imageVector = Icons.Filled.AccountBox,
                        contentDescription = "Profile button"
                    )
                }
                IconButton(onClick = { navController.navigate(Screen.Settings.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings button"
                    )
                }
            })
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.TempStartPage.name,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            composable(Screen.TempStartPage.name) {
                TempStartPage()
            }
            composable(Screen.StiturMap.name) {
                StiturMapScreen()
            }
            composable(Screen.Settings.name) {
                SettingsScreen()
            }
        }
    }
}