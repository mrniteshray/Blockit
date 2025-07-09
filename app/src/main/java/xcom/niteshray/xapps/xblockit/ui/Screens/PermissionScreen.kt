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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
            Text(
                text = "Permissions",
                fontSize = 28.sp,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if(dialog.value){
                AccessibilityPermissionDialog(onAllow = {
                    context.startActivity(
                        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                }) {
                    navController.navigate("main")
                    dialog.value = false
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
                            text = "Enable Permission",
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
                            text = "This will allow us to block shorts/reels and unwanted apps & websites",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Step 1 : Go to Accessibility Settings",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Step 2 : Click on Downloaded Apps",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Step 3 : Then Click on Blockit",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Step 4 : Enable Accessibility Service",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        GradientButton(text = "Enable Accessibility Service",modifier = Modifier.align(Alignment.CenterHorizontally),) {
                            try {
                                dialog.value = true
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(context, "Accessibility settings not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            //Run in Background Permission
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Accessibility Permission Required")
        },
        text = {
            Text(
                text = "Blockit uses the Accessibility Service to block short video content like YouTube Shorts and Instagram Reels, and also to help you stay away from distracting apps and websites.\n\n" +
                        "This permission allows us to detect which app is being used, so we can block unwanted content.\n\n" +
                        "We do not collect or store any of your personal data. Everything stays on your device.\n\n" +
                        "Do you want to continue and enable Accessibility Service?"
            )
        },
        confirmButton = {
            TextButton(onClick = onAllow) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No, thanks")
            }
        }
    )
}

