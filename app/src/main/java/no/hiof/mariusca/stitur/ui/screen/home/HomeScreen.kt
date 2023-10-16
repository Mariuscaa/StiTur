package no.hiof.mariusca.stitur.ui.screen.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.ui.screen.LeaderboardScreen
import no.hiof.mariusca.stitur.ui.screen.SignUpScreen
import no.hiof.mariusca.stitur.ui.screen.TempStartPage
import no.hiof.mariusca.stitur.ui.screen.map.StiturMapScreen
import no.hiof.mariusca.stitur.ui.screen.WeatherScreen



@Composable
fun HomeScreen() {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        //Text(text = "Navigation page")
        NavigationApp()
    }
}


sealed class Screen(val route: String, @StringRes val title: Int, val icon: ImageVector) {
    object Leaderboard : Screen("leaderboard", R.string.leaderboard, Icons.Default.Star)
    object StiturMap : Screen("maps", R.string.stiturMap, Icons.Default.LocationOn)
    object Profile : Screen("profile", R.string.profile, Icons.Default.AccountCircle)
    object Weather : Screen("weather", R.string.profile, Icons.Default.AccountCircle)

    object SignUp : Screen("SignUp", R.string.SignUp, Icons.Default.AccountCircle)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    //val signUpViewModel: SignUpViewModel = hiltViewModel()
    val bottomNavigationScreen = listOf(
        Screen.Leaderboard,
        Screen.StiturMap,
        Screen.Profile
    )

    Scaffold(bottomBar = { BottomNavBar(navController, bottomNavigationScreen) }) { innerPadding ->


        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {


            NavHost(
                navController = navController,
                startDestination = Screen.Profile.route /*, modifier = Modifier.padding(innerPadding)*/
            ) {
                composable(Screen.Leaderboard.route) {
                    LeaderboardScreen()

                }

                composable(Screen.StiturMap.route) {
                    //Text("Profile", modifier = Modifier.padding(innerPadding))
                    StiturMapScreen(weatherIconClicked = {
                        navController.navigate(Screen.Weather.route)
                    })

                }

                composable(Screen.Profile.route) {
                    //Text("Favourites", modifier = Modifier.padding(innerPadding))
                    TempStartPage()

                }

                composable(Screen.SignUp.route) {
                    SignUpScreen()
                }

                composable(Screen.Weather.route) {
                    WeatherScreen()
                }




            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, bottomNavigationScreen: List<Screen>) {
    val navBakStackEntry by navController.currentBackStackEntryAsState()
    val currentDeestination = navBakStackEntry?.destination?.route

    NavigationBar {
        bottomNavigationScreen.forEach { screen ->
            val title = screen.route
            NavigationBarItem(selected = currentDeestination == screen.route,
                onClick = {
                    navController.navigate(screen.route) {

                        popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                        }
                        launchSingleTop = true

                        //restoreState = true


                    }

                },
                icon = { Icon(imageVector = screen.icon, contentDescription = "Icon") },
                label = {
                    Text(title)

                }
            )
        }
    }
}

//Gammel kode under.

/*enum class Screen {
 StiturMap, Leaderboard, TempStartPage


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
            title = { Text(text = "StiTur") },
            actions = {
                /*IconButton(onClick = { navController.navigate(Screen.TempStartPage.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home button"
                    )
                }*/

                //Signuo
                IconButton(onClick = { navController.navigate(Screen.Leaderboard.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Leaderboard button"
                    )
                }

                //Signuo
                IconButton(onClick = { navController.navigate(Screen.StiturMap.name) }) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location button"
                    )
                }
                IconButton(onClick = { navController.navigate(Screen.TempStartPage.name) }) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Profile button"
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
            composable(Screen.Leaderboard.name) {
                SettingsScreen()
            }
            //composable(Screen.) {
            //    SignUpScreen(navController)
            //}
            // Jeg holder på å legge til Signup i menyen.
        }
    }
}
 */