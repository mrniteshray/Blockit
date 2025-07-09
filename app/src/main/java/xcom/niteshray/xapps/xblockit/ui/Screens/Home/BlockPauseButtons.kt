package xcom.niteshray.xapps.xblockit.ui.Screens.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import xcom.niteshray.xapps.xblockit.R
import xcom.niteshray.xapps.xblockit.util.BlockSharedPref

@Composable
fun BlockitControlButtons(
    isBlock: Boolean,
    onActiveClick: () -> Unit,
    onPauseClick: (Int) -> Unit
) {
    val context = LocalContext.current
    var showSliderDialog by remember { mutableStateOf(false) }
    var selectedDuration by remember { mutableStateOf(1f) }

    val blockSharedPref = remember { BlockSharedPref(context) }

    var remainingTime by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            remainingTime = blockSharedPref.getPauseEndTime()
            delay(1000)
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ControlButton(
            text = "Block",
            backgroundColor = if (isBlock) Color(
                0xFFD32F2F
            ) else Color.Black,
            onClick = onActiveClick,
            painter = painterResource(R.drawable.block),
            isBlock = isBlock
        )

        val pauseButtonText = if (remainingTime > 0) {
            val min = (remainingTime / 1000) / 60
            val sec = (remainingTime / 1000) % 60
            String.format("%02d:%02d", min, sec)
        } else {
            "Pause"
        }

        ControlButton(
            text = pauseButtonText,
            backgroundColor = if (!isBlock) Color.Yellow else Color.Black,
            onClick = { showSliderDialog = true },
            painter = painterResource(R.drawable.pause),
            isBlock
        )
    }

    if (showSliderDialog) {
        AlertDialog(
            onDismissRequest = { showSliderDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        onPauseClick(selectedDuration.toInt())
                        showSliderDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40C4FF)),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Start Pause", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showSliderDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Break Time")
                }
            },
            text = {
                Column {
                    Text("Duration: ${selectedDuration.toInt()} minutes")
                    Slider(
                        value = selectedDuration,
                        onValueChange = { selectedDuration = it },
                        valueRange = 1f..15f,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun ControlButton(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    painter: Painter,
    isBlock: Boolean
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp)
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(backgroundColor),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            elevation = ButtonDefaults.buttonElevation(6.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(4.dp))
                Image(
                    painter = painter,
                    contentDescription = "",
                    colorFilter = if(!isBlock && text!="Block") ColorFilter.tint(Color.Black) else ColorFilter.tint(Color.White),
                    modifier = Modifier.size(24.dp)
                )
                Text(text = text, color =
                    if(!isBlock && text!="Block") Color.Black else Color.White
                )
            }
        }
    }
}