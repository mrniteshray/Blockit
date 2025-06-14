package xcom.niteshray.xapps.xblockit.util

import android.content.Context
import java.net.URL

object BlockWesiteUtil {
    private val SHARED_PREF_NAME = "block_web"
    private val BLOCK_LIST = "block_list"
    fun SaveWebsite(context: Context  , url : Set<String>){
        val prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(BLOCK_LIST, url).apply()
    }
    fun GetWebsite(context: Context) : Set<String>{
        val prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(BLOCK_LIST, emptySet<String>()) ?: emptySet()
    }

    fun AddWebsite(context: Context,url : String){
        val base = getBaseUrl(url)
        val blocklist = GetWebsite(context).toMutableSet()
        blocklist.add(base)
        SaveWebsite(context, blocklist)
    }
    fun RemoveWebsite(context: Context,url : String){
        val base = getBaseUrl(url)
        val blocklist = GetWebsite(context).toMutableSet()
        blocklist.remove(base)
        SaveWebsite(context, blocklist)
    }

    private fun getBaseUrl(url: String): String {
        return try {
            val parsed = URL(url)
            "${parsed.protocol}://${parsed.host}".let {
                if (parsed.port == -1) it else "$it:${parsed.port}"
            }
        } catch (e: Exception) {
            url
        }
    }
}