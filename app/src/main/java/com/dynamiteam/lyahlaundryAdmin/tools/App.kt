package com.dynamiteam.lyahlaundryAdmin.tools

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefs = getSharedPreferences()
    }


    private fun getSharedPreferences() =
        instance.applicationContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun getStringApp(@StringRes stringId: Int) = instance.getString(stringId)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getDrawableApp(@DrawableRes drawableId: Int) = instance.getDrawable(drawableId)

    fun getColorApp(@ColorRes colorId: Int) = ContextCompat.getColor(instance, colorId)

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        MultiDex.install(this)

    }

}