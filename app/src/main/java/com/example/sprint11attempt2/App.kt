package com.example.sprint11attempt2

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson

class App : Application() {  // переключение на темную тему

    lateinit var sharedPrefs: SharedPreferences
        private set

    lateinit var gson: Gson
        private set

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        gson = Gson()

        if (sharedPrefs.getString(THEME_SWITCHER, "") == "") {
            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    switchTheme(true)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    switchTheme(false)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    switchTheme(darkTheme)
                }
            }
        } else {
            darkTheme = (sharedPrefs.getString(THEME_SWITCHER, "") == DARK_THEME_SWITCHER_ON)
            switchTheme(darkTheme)
        }

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
       const val THEME_SWITCHER = "THEME_SWITCHER"
       const val DARK_THEME_SWITCHER_ON = "on"
       const val DARK_THEME_SWITCHER_OFF = "off"


        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        lateinit var instance: App
            private set
    }
}