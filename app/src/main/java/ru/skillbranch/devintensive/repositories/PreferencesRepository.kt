package ru.skillbranch.devintensive.repositories

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.models.Profile

/**
 * Created by Svcat on 2019-08-04.
 */
object PreferencesRepository {

    private const val NICKNAME = "NICKNAME"
    private const val RANK = "RANK"
    private const val FIRSTNAME = "FIRSTNAME"
    private const val LASTNAME = "LASTNAME"
    private const val ABOUT = "ABOUT"
    private const val REPOSITORY = "REPOSITORY"
    private const val RATING = "RATING"
    private const val RESPECT = "RESPECT"
    private const val APP_THEME = "APP_THEME"

    private val prefs: SharedPreferences by lazy {
        val ctx = App.applicationContext()
        PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun saveAppTheme(value: Int) {
        putValue(APP_THEME to value)
    }

    fun getAppTheme(): Int = prefs.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_NO)

    fun getProfile(): Profile = Profile(
        prefs.getString(FIRSTNAME, "")!!,
        prefs.getString(LASTNAME, "")!!,
        prefs.getString(ABOUT, "")!!,
        prefs.getString(REPOSITORY, "")!!,
        prefs.getInt(RATING, 0),
        prefs.getInt(RESPECT, 0)
    )

    fun saveProfile(profile: Profile) {
        with(profile) {
            putValue(NICKNAME to profile.nickName)
            putValue(FIRSTNAME to profile.firstName)
            putValue(LASTNAME to profile.lastName)
            putValue(ABOUT to profile.about)
            putValue(REPOSITORY to profile.repository)
            putValue(RATING to profile.rating)
            putValue(RESPECT to profile.respect)
        }
    }

    private fun putValue(pair: Pair<String, Any>) = with(prefs.edit()) {
        val key = pair.first
        val value = pair.second

        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitives can be saved")
        }

        apply()
    }



}
