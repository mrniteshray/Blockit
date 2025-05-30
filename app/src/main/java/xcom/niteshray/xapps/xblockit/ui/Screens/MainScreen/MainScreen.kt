package xcom.niteshray.xapps.xblockit.ui.Screens.MainScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import xcom.niteshray.xapps.xblockit.model.Screen
import xcom.niteshray.xapps.xblockit.ui.Screens.Home.HomeScreen
import xcom.niteshray.xapps.xblockit.ui.Screens.Web.WebScreen
import xcom.niteshray.xapps.xblockit.ui.theme.Black
import androidx.compose.runtime.getValue
import xcom.niteshray.xapps.xblockit.ui.Screens.App.AppScreen
import xcom.niteshray.xapps.xblockit.ui.theme.lightblue


@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val screens = listOf(Screen.Home, Screen.App, Screen.Web)
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Black
            ){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(screen.title) },
                        icon = { Icon(screen.icon , contentDescription = screen.title) } ,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = lightblue,
                            selectedTextColor = lightblue,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        },
        containerColor = Color.Black
    ){ pd->
        Box(
            modifier = Modifier.padding(pd)
        ) {
            NavHost(navController = navController , startDestination = "home"){
                composable("home"){
                    HomeScreen()
                }
                composable("app"){
                    AppScreen()
                }
                composable("web"){
                    WebScreen()
                }
            }

        }
    }
}
