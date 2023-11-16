package no.hiof.mariusca.stitur.ui.screen.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import no.hiof.mariusca.stitur.R
import no.hiof.mariusca.stitur.model.Profile
import no.hiof.mariusca.stitur.signup.SignUpViewModel
import no.hiof.mariusca.stitur.ui.screen.GeoTreasureScreen
import no.hiof.mariusca.stitur.ui.screen.leaderboard.LeaderboardScreen
import no.hiof.mariusca.stitur.ui.screen.ProfileScreen
import no.hiof.mariusca.stitur.ui.screen.SignInScreen
import no.hiof.mariusca.stitur.ui.screen.SignUpScreen
import no.hiof.mariusca.stitur.ui.screen.WeatherScreen
import no.hiof.mariusca.stitur.ui.screen.leaderboard.StiturLeaderboardsViewModel
import no.hiof.mariusca.stitur.ui.screen.map.StiturMapScreen


@Composable
fun HomeScreen(viewModel: SignUpViewModel = hiltViewModel()) {
    val isAnonymous by viewModel.isAnonymous.collectAsState(initial = true)
    // You can add logic here based on `isAnonymous`
    if (isAnonymous) {
        // Handle anonymous user
    } else {
        val user by viewModel.currentLoggedInUser.collectAsState(initial = Profile())
        // Handle authenticated user
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        NavigationApp()

    }
}

//Alle sidene i prosjektet
sealed class Screen(val route: String, @StringRes val title: Int, val icon: Int) {
    object Leaderboard : Screen("Leaderboard", R.string.leaderboard, R.drawable.vector)
    object StiturMap : Screen("Maps", R.string.stiturMap, R.drawable.maps)
    object Profile : Screen("Profile", R.string.profile, R.drawable._icon__person_)
    object Weather : Screen("weather", R.string.profile, R.drawable.ic_weathericon)

    object SignUp : Screen("SignUp", R.string.SignUp, R.drawable.powerbutton)

    object SignIn : Screen("SignIn", R.string.SignUp, R.drawable.powerbutton)
    object GeoTreasure : Screen("GeoTreasure", R.string.SignUp,R.drawable.maps)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp() {

    val navController = rememberNavController()


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
                //startDestination = Screen.SignUp.route
                //startDestination = Screen.GeoTreasure.route
                startDestination = Screen.SignIn.route


            ) {
                composable(Screen.Leaderboard.route) {

                    val leaderboardsViewModel: StiturLeaderboardsViewModel = hiltViewModel()
                    LeaderboardScreen(viewModel = leaderboardsViewModel)
                }
                composable(Screen.StiturMap.route) {




                    //Text("Profile", modifier = Modifier.padding(innerPadding))
                    StiturMapScreen(weatherIconClicked = {
                        navController.navigate(Screen.Weather.route)
                    })
                        //,list = list)


                }
                composable(Screen.Profile.route) {
                    ProfileScreen(navController = navController)
                }
                composable(Screen.SignUp.route) {
                    SignUpScreen(navController = navController)
                }

                composable(Screen.SignIn.route) {
                    SignInScreen(navController = navController)
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
