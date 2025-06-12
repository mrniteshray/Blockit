package xcom.niteshray.xapps.xblockit.ui.Screens.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xcom.niteshray.xapps.xblockit.R
import com.airbnb.lottie.compose.*
import xcom.niteshray.xapps.xblockit.model.ShortBlockItem
import xcom.niteshray.xapps.xblockit.ui.theme.Blue

@Composable
fun HomeScreen(enableFocusMode : (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedMinutes by remember { mutableStateOf(25f) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val ShineBlue = Brush.linearGradient(
        colors = listOf(
            Color(0xFF00B4DB),
            Color(0xFF0083B0)
        )
    )


    val apps = listOf(
        ShortBlockItem("Instagram Reels", R.drawable.reel,"com.instagram.android", false),
        ShortBlockItem("Youtube Shorts", R.drawable.shorts,"com.google.android.youtube", false),
        ShortBlockItem("SnapChat Spotlight", R.drawable.snapchat,"com.snapchat.android",false),
        ShortBlockItem("Facebook Reels", R.drawable.facebook,"com.facebook.katana",false)
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Blockit",
            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Stay Focused To Your Goals",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        GradientButton(text = "Enable Focus Mode",modifier = Modifier.align(Alignment.CenterHorizontally)) {
            showDialog = true
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        enableFocusMode(selectedMinutes.toInt())
                    }) {
                        Text("Start")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Select Focus Duration", modifier = Modifier.align(Alignment.CenterHorizontally)) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${selectedMinutes.toInt()} minutes",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Slider(
                            value = selectedMinutes,
                            onValueChange = { selectedMinutes = it },
                            valueRange = 5f..60f
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        ShortsBlockUI(apps)
    }
}



