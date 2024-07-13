package xyz.droidev.notelab.data.local

import android.content.Context

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
class PrefManager(
    private val context : Context
){

    private val pref = context.getSharedPreferences("notelab", Context.MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean){
        pref.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    fun saveString(key: String, value: String){
        pref.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return pref.getString(key, null)
    }


    fun clear(){
        pref.edit().clear().apply()
    }

    fun remove(key: String){
        pref.edit().remove(key).apply()
    }

    object Keys{
        const val IS_LOGGED_IN = "is_logged_in"
        const val USER_ID = "user_id"
    }
}