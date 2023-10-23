package no.hiof.mariusca.stitur.ui.screen.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.ui.screen.GeoTreasureScreen
import no.hiof.mariusca.stitur.ui.screen.LeaderboardScreen
import no.hiof.mariusca.stitur.ui.screen.ProfileScreen
import no.hiof.mariusca.stitur.ui.screen.SignUpScreen
import no.hiof.mariusca.stitur.ui.screen.WeatherScreen
import no.hiof.mariusca.stitur.ui.screen.map.StiturMapScreen


@Composable
fun HomeScreen() {

    Column (
        horizontalAlignment = Alignment.CenterHorizontally) {
        NavigationApp()
    }
}

//Alle sidene i prosjektet
sealed class Screen(val route: String, @StringRes val title: Int, val icon: Int) {
    object Leaderboard : Screen("leaderboard", R.string.leaderboard, R.drawable.vector)
    object StiturMap : Screen("maps", R.string.stiturMap, R.drawable.maps)
    object Profile : Screen("profile", R.string.profile, R.drawable.profile)
    object Weather : Screen("weather", R.string.profile, R.drawable.ic_weathericon)

    object SignUp : Screen("SignUp", R.string.SignUp, R.drawable.profile)
    object GeoTreasure : Screen("GeoTreasure", R.string.SignUp,R.drawable.maps)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp() {
    //val customBackgroundColor = Color(0xFF133c07)


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

                //.background(customBackgroundColor),
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
                    ProfileScreen()
                }
                composable(Screen.SignUp.route) {
                    SignUpScreen()
                }


                composable(Screen.Weather.route) {
                    WeatherScreen()
                }


                composable(Screen.GeoTreasure.route) {
                    GeoTreasureScreen()
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
                icon = { Icon(painterResource(id = screen.icon), contentDescription = "Icon",
                    modifier = Modifier
                        .size(20.dp),
                    ) },

                label = {
                    Text(title)
                }
            )
        }
    }
}
