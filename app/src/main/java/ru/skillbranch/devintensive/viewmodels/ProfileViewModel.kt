package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

/**
 * Created by Svcat on 2019-08-04.
 */
class ProfileViewModel : ViewModel() {

    private val repository: PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()
    private val repoValid = MutableLiveData<Boolean>()


    init {
        Log.d("hi", "init view model")
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("hi", "clearing view model")
    }

    fun getProfileData(): LiveData<Profile> = profileData

    fun saveProfileData(profile: Profile) {
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun switchTheme() {
        if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun getTheme(): LiveData<Int> = appTheme

    fun getRepoValid(): LiveData<Boolean> = repoValid

    fun validateRepo(repository: String) {
        repoValid.value = repository.isEmpty() || repository.matches(
                Regex("^(https://)?(www.)?(github.com/)(?!(${getRegexExceptions()})(?=/|$))(?![\\W])(?!\\w+[-]{2})[a-zA-Z0-9-]+(?<![-])(/)?$")
        )
    }

    private fun getRegexExceptions(): String {
        val exceptions = arrayOf(
                "enterprise",
                "features",
                "topics",
                "collections",
                "trending",
                "events",
                "join",
                "pricing",
                "nonprofit",
                "customer-stories",
                "security",
                "login",
                "marketplace"
        )
        return exceptions.joinToString("|")
    }
}
