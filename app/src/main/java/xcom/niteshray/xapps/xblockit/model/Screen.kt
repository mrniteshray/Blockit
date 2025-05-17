package xcom.niteshray.xapps.xblockit.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector){
    object Home : Screen("home","Home", Icons.Default.Home)
    object App : Screen("app","App",Icons.Default.Warning)
    object Web : Screen("web","Web",Icons.Default.Star)
}