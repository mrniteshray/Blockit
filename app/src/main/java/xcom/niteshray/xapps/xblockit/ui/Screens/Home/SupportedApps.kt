package xcom.niteshray.xapps.xblockit.ui.Screens.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import xcom.niteshray.xapps.xblockit.R
import xcom.niteshray.xapps.xblockit.model.ShortBlockItem


@Composable
fun SupportedApps() {
    val apps = listOf(
        ShortBlockItem("Instagram Reels", R.drawable.reel, "com.instagram.android", false),
        ShortBlockItem("YouTube Shorts", R.drawable.shorts, "com.google.android.youtube", false),
        ShortBlockItem("Snapchat Spotlight", R.drawable.snapchat, "com.snapchat.android", false),
        ShortBlockItem("Facebook Reels", R.drawable.facebook, "com.facebook.katana", false)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFF1A1A1A))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(30.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "🎯 Supported Apps",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFEEEEEE),
            modifier = Modifier.padding(bottom = 8.dp)
        )



        apps.forEach { app ->
            SupportedAppRow(app)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun SupportedAppRow(app: ShortBlockItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2B2B2B))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = app.iconResId),
            contentDescription = app.name,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = app.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE0E0E0),
            modifier = Modifier.weight(1f)
        )
    }
}

