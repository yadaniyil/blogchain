package com.yadaniil.app.blogchain.data.prefs

import android.content.SharedPreferences
import com.yadaniil.app.blogchain.Application
import javax.inject.Inject

/**
 * Created by danielyakovlev on 7/1/17.
 */
class SharedPrefs : SharedPrefsHelper {

    // region Keys
    val LAST_SHOW_CHANGELOG_VERSION = "last_show_changelog_version"
    // endregion Keys

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    init {
        Application.component?.inject(this)
    }


    override fun getLastShowChangelogVersion() = getIntByKey(LAST_SHOW_CHANGELOG_VERSION)
    override fun setLastShowChangelogVersion(versionCode: Int)
            = saveInt(LAST_SHOW_CHANGELOG_VERSION, versionCode)


    // region General helping methods
    private fun saveString(key: String, value: String) = sharedPrefs.edit().putString(key, value).apply()

    private fun remove(key: String) = sharedPrefs.edit().remove(key).apply()
    private fun getStringByKey(key: String): String = sharedPrefs.getString(key, "")
    private fun saveBoolean(key: String, value: Boolean) = sharedPrefs.edit().putBoolean(key, value).apply()
    private fun getBooleanByKey(key: String): Boolean = sharedPrefs.getBoolean(key, false)
    private fun saveInt(key: String, value: Int) = sharedPrefs.edit().putInt(key, value).apply()
    private fun getIntByKey(key: String): Int = sharedPrefs.getInt(key, 0)
    // endregion General helping methods
}