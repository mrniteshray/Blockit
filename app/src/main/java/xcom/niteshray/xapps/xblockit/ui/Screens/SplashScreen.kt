package xcom.niteshray.xapps.xblockit.ui.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import xcom.niteshray.xapps.xblockit.ui.theme.Black
import xcom.niteshray.xapps.xblockit.util.BlockAccessibility

@Composable
fun SplashScreen(navController: NavController){
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(1000)
        if (isAccessibilityServiceEnabled(context, BlockAccessibility::class.java) && isIgnoringBatteryOptimizations(context)) {
            navController.navigate("main"){
                popUpTo("splash"){inclusive = true}
            }
        }else{
            navController.navigate("permission"){
                popUpTo("splash"){inclusive = true}
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Blockit",
            fontSize = 32.sp ,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}