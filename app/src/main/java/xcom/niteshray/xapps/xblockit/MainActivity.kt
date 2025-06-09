package xcom.niteshray.xapps.xblockit

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
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
import android.provider.Settings
import android.accessibilityservice.AccessibilityService
import androidx.navigation.NavType
import androidx.navigation.navArgument
import xcom.niteshray.xapps.xblockit.ui.Screens.FocusScreen
import xcom.niteshray.xapps.xblockit.ui.Screens.PermissionScreen


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
            composable("permission"){
                PermissionScreen(navController)
            }
            composable(route = "main"){
                MainScreen(navController)
            }
            composable(route = "focus_screen/{duration}", arguments = listOf(navArgument("duration") { type = NavType.IntType })){ backStackEntry ->
                val duration = backStackEntry.arguments?.getInt("duration") ?: 0
                FocusScreen(duration){
                    navController.popBackStack()
                }
            }
        }
    }
}

fun isAccessibilityServiceEnabled(context: Context, service: Class<out AccessibilityService>): Boolean {
    val expectedComponentName = ComponentName(context, service)
    val enabledServicesSetting = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
    val colonSplitter = TextUtils.SimpleStringSplitter(':')

    colonSplitter.setString(enabledServicesSetting ?: return false)
    return colonSplitter.any {
        ComponentName.unflattenFromString(it)?.equals(expectedComponentName) == true
    }
}

