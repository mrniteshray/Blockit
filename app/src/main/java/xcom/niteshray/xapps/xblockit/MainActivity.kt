package xcom.niteshray.xapps.xblockit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import xcom.niteshray.xapps.xblockit.ui.Screens.MainScreen.MainScreen
import xcom.niteshray.xapps.xblockit.ui.Screens.SplashScreen
import xcom.niteshray.xapps.xblockit.ui.theme.BlockitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlockitTheme {
                App()
            }
        }
    }
    @Composable
    fun App(){
        val navController = rememberNavController()
        NavHost(navController = navController , startDestination = "splash"){
            composable("splash"){
                SplashScreen(navController)
            }
            composable(route = "main"){
                MainScreen()
            }
        }
    }
}
