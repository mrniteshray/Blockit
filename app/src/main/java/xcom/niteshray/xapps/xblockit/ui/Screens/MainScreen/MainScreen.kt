package xcom.niteshray.xapps.xblockit.ui.Screens.MainScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import xcom.niteshray.xapps.xblockit.ui.Screens.App.AppScreen
import xcom.niteshray.xapps.xblockit.ui.theme.lightblue


@Composable
fun MainScreen(navController: NavController){
    val innerNavControlller = rememberNavController()
    val screens = listOf(Screen.Home, Screen.App, Screen.Web)
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Black
            ){
                val navBackStackEntry by innerNavControlller.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            innerNavControlller.navigate(screen.route) {
                                popUpTo(innerNavControlller.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(screen.title) },
                        icon = {
                            when (screen.icon) {
                                is ImageVector -> Icon(
                                    screen.icon, contentDescription = screen.title , modifier = Modifier.size(24.dp))
                                is Int -> Icon(painterResource(id = screen.icon), contentDescription = screen.title, modifier = Modifier.size(24.dp))
                                else -> {  }
                            }
                               } ,
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
            NavHost(navController = innerNavControlller , startDestination = "home"){
                composable("home"){
                    HomeScreen(navController)
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
