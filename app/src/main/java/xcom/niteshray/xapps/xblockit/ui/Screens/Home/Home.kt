package xcom.niteshray.xapps.xblockit.ui.Screens.Home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xcom.niteshray.xapps.xblockit.R
import com.airbnb.lottie.compose.*
import xcom.niteshray.xapps.xblockit.model.ShortBlockItem

@Composable
fun HomeScreen(enableFocusMode : (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedMinutes by remember { mutableStateOf(25f) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
    val context = LocalContext.current

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    var showPrivacyDialog by remember { mutableStateOf(false) }


    val apps = listOf(
        ShortBlockItem("Instagram Reels", R.drawable.reel,"com.instagram.android", false),
        ShortBlockItem("Youtube Shorts", R.drawable.shorts,"com.google.android.youtube", false),
        ShortBlockItem("SnapChat Spotlight", R.drawable.snapchat,"com.snapchat.android",false),
        ShortBlockItem("Facebook Reels", R.drawable.facebook,"com.facebook.katana",false)
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = { showPrivacyDialog = true }) {
                Icon(
                    imageVector =Icons.Default.Info,
                    contentDescription = "Privacy Policy",
                    tint = Color.White
                )
            }
        }
        if (showPrivacyDialog) {
            AlertDialog(
                onDismissRequest = { showPrivacyDialog = false },
                confirmButton = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                showPrivacyDialog = false
                                val uri = "https://buymeacoffee.com/im_nitesh"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD700),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                                Text(
                                    text = "Support Me",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Button(
                            onClick = {
                                showPrivacyDialog = false
                                val uri = "https://mrniteshray.github.io/Blockit/PRIVACY_POLICY"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF40C4FF),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Privacy Policy",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },
                dismissButton = {

                },
                title = {
                    Text(
                        text = "Support & Privacy",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                },
                text = {
                    Text(
                        text = "Support the app or view our Privacy Policy.",
                        color = Color(0xFFB0BEC5),
                        textAlign = TextAlign.Center
                    )
                },
                containerColor = Color(0xFF212121), // Dark gray background
                titleContentColor = Color.White,
                textContentColor = Color(0xFFB0BEC5)
            )
        }
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
        Spacer(modifier = Modifier.height(10.dp))
        ShortsBlockUI(apps)
    }
}



