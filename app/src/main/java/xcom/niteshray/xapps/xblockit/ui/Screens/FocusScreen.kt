package xcom.niteshray.xapps.xblockit.ui.Screens

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import xcom.niteshray.xapps.xblockit.ui.theme.gray

@Composable
fun FocusScreen(duration: Int, onExit: () -> Boolean) {
        val context = LocalContext.current
        val activity = context as? Activity
        val notificationmng = remember { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager}

        LaunchedEffect(Unit) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            if (!notificationmng.isNotificationPolicyAccessGranted){
                Toast.makeText(context,"Please Allow Notification Permission",Toast.LENGTH_LONG).show()
                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            }else{
                notificationmng.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                if(notificationmng.isNotificationPolicyAccessGranted){
                    notificationmng.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                }
            }
        }

        var timeLeft by remember { mutableStateOf(duration * 60) }
        LaunchedEffect(key1 = timeLeft) {
            if (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1
            } else {
                onExit()
            }
        }
        BackHandler {
            onExit()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            val minutes = timeLeft / 60
            val seconds = timeLeft % 60
            val timeText = String.format("%02d:%02d", minutes, seconds)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = timeText,
                    fontSize = 180.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Time is yours. Use it well",
                    fontSize = 24.sp,
                    color = gray,
                    textAlign = TextAlign.Center
                )
            }

        }
 }