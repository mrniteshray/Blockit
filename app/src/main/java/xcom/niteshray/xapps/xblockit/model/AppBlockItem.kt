package xcom.niteshray.xapps.xblockit.model

data class AppBlockItem(
    val name: String,
    val iconResId: Int,
    val packageName: String,
    var isEnabled: Boolean = false
)
