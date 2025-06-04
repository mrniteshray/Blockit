package xcom.niteshray.xapps.xblockit.model

import android.graphics.drawable.Drawable

data class Appitem(
    val name : String,
    val Icon : Drawable,
    val packageName : String,
    var isBlock : Boolean
)