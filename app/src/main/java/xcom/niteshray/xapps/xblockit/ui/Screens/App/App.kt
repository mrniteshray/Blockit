package xcom.niteshray.xapps.xblockit.ui.Screens.App

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xcom.niteshray.xapps.xblockit.model.Appitem
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import xcom.niteshray.xapps.xblockit.ui.theme.Black
import xcom.niteshray.xapps.xblockit.ui.theme.Blue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(viewModel: AppViewModel = viewModel()) {
    val apps = viewModel.app

    var searchApp = remember { mutableStateOf("") }
    val context = LocalContext.current

    if (apps.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val filteredApps = apps.filter { app ->
            app.name.contains(searchApp.value, ignoreCase = true)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Block Apps",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color(0xFF2C2C2C))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = searchApp.value,
                        onValueChange = { searchApp.value = it },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (searchApp.value.isEmpty()) {
                                Text(
                                    text = "Search apps...",
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
            LazyColumn(
            ) {
                items(filteredApps) { app ->
                    AppItems(app) { isBlocked ->
                        viewModel.updateBlock(app.packageName, isBlocked)
                        if (isBlocked) Toast.makeText(context,app.name+" Blocked",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
@Composable
fun AppItems(app: Appitem, onToggleChange: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(app.isBlock) }
    val painter = remember(app.Icon) {
        BitmapPainter(app.Icon.toBitmap().asImageBitmap())
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1C1C1E))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = app.name,
            modifier = Modifier
                .size(30.dp)
                .padding(end = 12.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = app.name,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                app.isBlock = it
                onToggleChange(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Black,
                uncheckedThumbColor = Color.Gray,
                checkedTrackColor = Blue,
                uncheckedTrackColor = Black,
            )
        )
    }
}