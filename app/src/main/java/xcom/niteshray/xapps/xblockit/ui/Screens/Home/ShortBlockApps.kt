package xcom.niteshray.xapps.xblockit.ui.Screens.Home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import xcom.niteshray.xapps.xblockit.model.ShortBlockItem


@Composable
fun ShortsBlockUI(apps: List<ShortBlockItem>) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("BlockedApps", Context.MODE_PRIVATE)

    val updatedApps = remember {
        apps.map {
            it.copy(isEnabled = sharedPref.getBoolean(it.packageName, false))
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        item {
            Text(
                text = "Block Shorts",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(updatedApps) { app ->
            BlockItem(app){
                app.isEnabled = it
                with(sharedPref.edit()) {
                    putBoolean(app.packageName, it)
                    apply()
                }
                if (it) Toast.makeText(context,app.name+" Blocked",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun BlockItem(app: ShortBlockItem, onToggleChange: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(app.isEnabled) }

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
            painter = painterResource(id = app.iconResId),
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
                app.isEnabled = it
                onToggleChange(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF5E9CFA),
                uncheckedThumbColor = Color.Gray
            )
        )
    }
}
