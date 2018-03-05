package com.yadaniil.blogchain.data.prefs

import android.content.SharedPreferences
import com.google.gson.Gson
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcGlobalDataResponse
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcMarketCapAndVolumeChartResponse

/**
 * Created by danielyakovlev on 7/1/17.
 */
class SharedPrefs(private val sharedPrefs: SharedPreferences) : SharedPrefsHelper {

    // region Keys
    private val LAST_SHOW_CHANGELOG_VERSION = "last_show_changelog_version"
    private val LAST_COINS_UPDATE_TIME = "last_coins_update_time"
    private val SHOW_PORTFOLIO_AT_HOME = "show_portfolio_at_home"
    private val CMC_GLOBAL_DATA = "cmc_global_data"
    private val CMC_GLOBAL_DATA_CHARTS = "cmc_global_data_charts"
    // endregion Keys

    override fun getLastShowChangelogVersion() = getIntByKey(LAST_SHOW_CHANGELOG_VERSION)
    override fun setLastShowChangelogVersion(versionCode: Int) = saveInt(LAST_SHOW_CHANGELOG_VERSION, versionCode)

    override fun saveLastCoinsUpdateTime(lastUpdateTime: Long) = saveLong(LAST_COINS_UPDATE_TIME, lastUpdateTime)
    override fun getLastCoinsUpdateTime() = getLongByKey(LAST_COINS_UPDATE_TIME)

    override fun setShowPortfolioAtHome(showPortfolioAtHome: Boolean) = saveBoolean(SHOW_PORTFOLIO_AT_HOME, showPortfolioAtHome)
    override fun getShowPortfolioAtHome() = getBooleanByKey(SHOW_PORTFOLIO_AT_HOME, true)

    override fun saveCmcGlobalData(data: CmcGlobalDataResponse?) = saveString(CMC_GLOBAL_DATA, Gson().toJson(data))
    override fun getCmcGlobalData(): CmcGlobalDataResponse? {
        val json = getStringByKey(CMC_GLOBAL_DATA)
        return if (json.isEmpty())
            null
        else
            Gson().fromJson<CmcGlobalDataResponse>(json, CmcGlobalDataResponse::class.java)
    }

    override fun saveCmcMarketCapAndVolumeChartData(data: CmcMarketCapAndVolumeChartResponse?) = saveString(CMC_GLOBAL_DATA_CHARTS, Gson().toJson(data))

    override fun getCmcMarketCapAndVolumeChartData(): CmcMarketCapAndVolumeChartResponse? {
        val json = getStringByKey(CMC_GLOBAL_DATA_CHARTS)
        return if (json.isEmpty())
            null
        else
            Gson().fromJson<CmcMarketCapAndVolumeChartResponse>(json, CmcMarketCapAndVolumeChartResponse::class.java)
    }

    // region General helping methods
    private fun saveString(key: String, value: String) = sharedPrefs.edit().putString(key, value).apply()

    private fun remove(key: String) = sharedPrefs.edit().remove(key).apply()
    private fun getStringByKey(key: String): String = sharedPrefs.getString(key, "")
    private fun saveBoolean(key: String, value: Boolean) = sharedPrefs.edit().putBoolean(key, value).apply()
    private fun getBooleanByKey(key: String, default: Boolean? = null): Boolean = sharedPrefs.getBoolean(key, default
            ?: false)

    private fun saveInt(key: String, value: Int) = sharedPrefs.edit().putInt(key, value).apply()
    private fun getIntByKey(key: String): Int = sharedPrefs.getInt(key, 0)
    private fun saveLong(key: String, value: Long) = sharedPrefs.edit().putLong(key, value).apply()
    private fun getLongByKey(key: String): Long = sharedPrefs.getLong(key, 0)
    // endregion General helping methods
}