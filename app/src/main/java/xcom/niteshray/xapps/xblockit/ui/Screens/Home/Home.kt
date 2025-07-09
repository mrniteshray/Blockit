package xcom.niteshray.xapps.xblockit.ui.Screens.Home

import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import xcom.niteshray.xapps.xblockit.R
import com.airbnb.lottie.compose.*
import xcom.niteshray.xapps.xblockit.ui.Screens.isAccessibilityServiceEnabled
import xcom.niteshray.xapps.xblockit.util.BlockAccessibility
import xcom.niteshray.xapps.xblockit.util.BlockSharedPref
import xcom.niteshray.xapps.xblockit.util.PauseTimeService

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val blockSharedPref = BlockSharedPref(context)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    var showPrivacyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
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
        var isBlock by remember { mutableStateOf(blockSharedPref.getBlock()) }
        BlockitControlButtons(
            isBlock = isBlock,
            onActiveClick = {
                if(isAccessibilityServiceEnabled(context,BlockAccessibility::class.java)){
                    blockSharedPref.setBlock(true)
                    blockSharedPref.setPauseEndTime(0L)
                    val intent = Intent(context, PauseTimeService::class.java)
                    context.stopService(intent)
                    isBlock = true
                }else{
                    navController.navigate("permission")
                }
            },
            onPauseClick = { minutes ->
                val intent = Intent(context , PauseTimeService::class.java)
                intent.putExtra("pause_time",minutes * 60 * 1000L)
                context.startService(intent)
                blockSharedPref.setBlock(false)
                isBlock = false
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        SupportedApps()
    }
}



