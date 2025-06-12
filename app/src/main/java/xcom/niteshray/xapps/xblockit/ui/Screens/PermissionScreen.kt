package xcom.niteshray.xapps.xblockit.ui.Screens

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import xcom.niteshray.xapps.xblockit.R
import xcom.niteshray.xapps.xblockit.isAccessibilityServiceEnabled
import xcom.niteshray.xapps.xblockit.ui.Screens.Home.GradientButton
import xcom.niteshray.xapps.xblockit.ui.theme.Blue
import xcom.niteshray.xapps.xblockit.util.BlockAccessibility
import xcom.niteshray.xapps.xblockit.util.NotificationHelper

@Composable
fun PermissionScreen(navController: NavHostController) {
    val context = LocalContext.current
    val isEnabled = remember { mutableStateOf(isAccessibilityServiceEnabled(context, BlockAccessibility::class.java)) }

    val lifecycleOwner = LocalLifecycleOwner.current
    NotificationHelper(context).checkAndRequestPermission()
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isEnabled.value = isAccessibilityServiceEnabled(context, BlockAccessibility::class.java)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(isEnabled.value) {
        if (isEnabled.value) {
            navController.navigate("main") {
                popUpTo("permission") { inclusive = true }
            }
        }
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color.Black).padding(4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ){

            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(R.drawable.perm_illus),
                contentDescription = "Permission Illustration",
                modifier = Modifier.height(300.dp).width(300.dp).align(Alignment.CenterHorizontally)
            )
            Box {
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Text(
                        text = "Enable Permission",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
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
                        context.startActivity(
                            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        )
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "Accessibility settings not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }



        }
    }

}