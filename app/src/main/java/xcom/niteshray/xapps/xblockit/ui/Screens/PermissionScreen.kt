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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import xcom.niteshray.xapps.xblockit.isAccessibilityServiceEnabled
import xcom.niteshray.xapps.xblockit.ui.theme.Blue
import xcom.niteshray.xapps.xblockit.util.BlockAccessibility

@Composable
fun PermissionScreen(navController: NavHostController) {
    val context = LocalContext.current
    val isEnabled = remember { mutableStateOf(isAccessibilityServiceEnabled(context, BlockAccessibility::class.java)) }

    val lifecycleOwner = LocalLifecycleOwner.current
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
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color.Black).padding(4.dp)
        ){
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enable Permission",
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
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
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Step 1 : Go to Accessibility Settings",
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Step 2 : Click on Downloaded Apps",
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Step 3 : Then Click on Blockit",
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Step 4 : Enable Accessibility Service",
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                try {
                    context.startActivity(
                        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "Accessibility settings not found", Toast.LENGTH_SHORT).show()
                }
            },colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = Color.White
            ),
                modifier = Modifier.align(Alignment.CenterHorizontally)

            ) {
                Text(
                    text = "Enable Accessibility Service",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }


        }
    }

}