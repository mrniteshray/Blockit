package xcom.niteshray.xapps.xblockit.util

import android.content.Context

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
        val blocklist = GetWebsite(context).toMutableSet()
        blocklist.add(url)
        SaveWebsite(context,blocklist)
    }
    fun RemoveWebsite(context: Context,url : String){
        val blocklist = GetWebsite(context).toMutableSet()
        blocklist.remove(url)
        SaveWebsite(context,blocklist)
    }
}