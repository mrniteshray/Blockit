package xcom.niteshray.xapps.xblockit.model

data class ShortBlockItem(
    val name: String,
    val iconResId: Int,
    val packageName: String,
    var isEnabled: Boolean = false
)
