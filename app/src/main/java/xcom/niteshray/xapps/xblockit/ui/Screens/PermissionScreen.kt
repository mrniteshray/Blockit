package xcom.niteshray.xapps.xblockit.ui.Screens

import android.accessibilityservice.AccessibilityService
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import xcom.niteshray.xapps.xblockit.R
import xcom.niteshray.xapps.xblockit.ui.Screens.Home.GradientButton
import xcom.niteshray.xapps.xblockit.ui.theme.Blue
import xcom.niteshray.xapps.xblockit.util.BlockAccessibility
import xcom.niteshray.xapps.xblockit.util.NotificationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScreen(navController: NavHostController) {
    val context = LocalContext.current
    val isAccessibilityEnable = remember { mutableStateOf(isAccessibilityServiceEnabled(context, BlockAccessibility::class.java)) }
    val isAllowBackgroundRun = remember { mutableStateOf(isIgnoringBatteryOptimizations(context))}
    var dialog = remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    NotificationHelper(context).checkAndRequestPermission()
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isAccessibilityEnable.value = isAccessibilityServiceEnabled(context, BlockAccessibility::class.java)
                isAllowBackgroundRun.value = isIgnoringBatteryOptimizations(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(isAccessibilityEnable.value , isAllowBackgroundRun.value) {
        if (isAccessibilityEnable.value && isAllowBackgroundRun.value) {
            dialog.value = false
            navController.navigate("main") {
                popUpTo("permission") { inclusive = true }
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            Image(
                painter = painterResource(R.drawable.nextbtn),
                contentDescription = "next_btn",
                modifier = Modifier.padding(15.dp).size(55.dp).clickable{
                    navController.navigate("main"){
                        popUpTo("permission") { inclusive = true }
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color.Black).padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Permissions",
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
                Text(
                    text = "Skip",
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterEnd).clickable{
                        navController.navigate("main"){
                            popUpTo("permission") { inclusive = true }
                        }
                    }
                )
            }


            if(dialog.value){
                AccessibilityPermissionDialog(onAllow = {
                    context.startActivity(
                        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                }) {
                    dialog.value = false
                    Toast.makeText(context, "You can enable accessibility later from settings", Toast.LENGTH_SHORT).show()
                }
            }

            if(!isAccessibilityEnable.value){
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(12.dp)
                        .background(Color(0xFF1C1C1E), RoundedCornerShape(12.dp))
                        .padding(24.dp)
                ){
                    Column(
                    ){
                        Text(
                            text = "Need Permission",
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Blue
                        )
                        Text(
                            text = "Accessibility Service",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Blue
                        )
                        Text(
                            text = "This will allow us to block scrolling and unwanted apps & websites",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "Blockit is 100% secure and DOES NOT\nmonitor/collect any personal data.",
                            color = Color(0xFF00FF6A),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        GradientButton(text = "Agree",modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),) {
                            try {
                                dialog.value = true
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(context, "Accessibility settings not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            if(!isAllowBackgroundRun.value){
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(12.dp)
                        .background(Color(0xFF1C1C1E), RoundedCornerShape(12.dp))
                        .padding(24.dp)
                ){
                    Column(
                    ){
                        Text(
                            text = "Keep App Running in Background",
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Blue
                        )
                        Text(
                            text = "To keep blocking distractions in real time, we need to stay active in the background.",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        GradientButton(text = "Allow Permission",modifier = Modifier.align(Alignment.CenterHorizontally),) {
                            try {
                                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                    data = Uri.parse("package:${context.packageName}")
                                }
                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(context, "Accessibility settings not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
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

fun isIgnoringBatteryOptimizations(context: Context): Boolean {
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return pm.isIgnoringBatteryOptimizations(context.packageName)
}

@Composable
fun AccessibilityPermissionDialog(
    onAllow: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000)), // semi-transparent overlay
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C1F26)
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Accessibility Permission Required",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "To detect and block short videos in real-time, we need accessibility service permission.",
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                val stepItems = listOf(
                    "Tap On Agree Button",
                    "Select Installed Apps",
                    "Enable Blockit"
                )

                stepItems.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color(0xFF2A2E39), RoundedCornerShape(12.dp))
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = "${index + 1}.  $item",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Blockit is 100% secure and DOES NOT\nmonitor/collect any personal data.",
                    color = Color(0xFF00FF6A),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Not Now", color = Color.White)
                    }

                    Button(
                        onClick = onAllow,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Agree", color = Color.White)
                    }
                }
            }
        }
    }
}



