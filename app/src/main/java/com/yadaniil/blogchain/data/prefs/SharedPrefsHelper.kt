package com.yadaniil.blogchain.data.prefs

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface SharedPrefsHelper {

    fun getLastShowChangelogVersion(): Int
    fun setLastShowChangelogVersion(versionCode: Int)

}