package xcom.niteshray.xapps.xblockit.ui.Screens

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import xcom.niteshray.xapps.xblockit.ui.theme.Black

@Composable
fun SplashScreen(navController: NavController){

    LaunchedEffect(Unit) {
        delay(1000)
        navController.navigate("main")
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