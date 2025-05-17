package xcom.niteshray.xapps.xblockit.ui.Screens.Home

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xcom.niteshray.xapps.xblockit.R
import com.airbnb.lottie.compose.*
import xcom.niteshray.xapps.xblockit.model.AppBlockItem

@Composable
fun HomeScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val apps = listOf(
        AppBlockItem("Instagram Reels", R.drawable.reel,"com.instagram.android", false),
        AppBlockItem("Youtube Shorts", R.drawable.shorts,"com.google.android.youtube", false),
        AppBlockItem("SnapChat Spotlight", R.drawable.snapchat,"com.snapchat.android",false),
        AppBlockItem("Facebook Reels", R.drawable.facebook,"com.facebook.katana",false)
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
        Spacer(modifier = Modifier.height(40.dp))
        ShortsBlockUI(apps)
    }
}
