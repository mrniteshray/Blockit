package xcom.niteshray.xapps.xblockit.util

import android.content.Context

class BlockSharedPref(context : Context){
    private val SHARED_PREF_NAME = "BlockSharedPref"
    private val KEY_BLOCK = "BLOCKED"
    private val KEY_PAUSE_END_TIME = "PAUSE_END_TIME"

    private val sharedPref =
        context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)

    fun setBlock(value : Boolean){
        sharedPref.edit().putBoolean(KEY_BLOCK , value).apply()
    }

    fun getBlock() : Boolean{
        return sharedPref.getBoolean(KEY_BLOCK , false)
    }
    fun setPauseEndTime(Time: Long) {
        sharedPref.edit().putLong(KEY_PAUSE_END_TIME, Time).apply()
    }

    fun getPauseEndTime(): Long {
        return sharedPref.getLong(KEY_PAUSE_END_TIME, 0L)
    }
}