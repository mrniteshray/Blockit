package xcom.niteshray.xapps.xblockit.util

import android.content.Context

object BlockAppsUtil {
    private val SHARED_PREF_NAME = "block_apps"
    private val BLOCK_LIST = "block_list"

    fun saveBlockApp(context : Context , blockedlist : Set<String>){
        val prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(BLOCK_LIST , blockedlist).apply()
    }

    fun getBlockApps(context: Context) : Set<String>{
        val prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(BLOCK_LIST,emptySet<String>()) ?: emptySet()
    }

    fun addBlockApp(context: Context,packageName : String){
        val blocklist = getBlockApps(context).toMutableSet()
        blocklist.add(packageName)
        saveBlockApp(context,blocklist)
    }

    fun removeBlockApp(context: Context,packageName : String){
        val blockedlist = getBlockApps(context).toMutableSet()
        blockedlist.remove(packageName)
        saveBlockApp(context,blockedlist)
    }
}