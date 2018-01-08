package com.yadaniil.blogchain.data.prefs

import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcGlobalDataResponse
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcMarketCapAndVolumeChartResponse


/**
 * Created by danielyakovlev on 7/1/17.
 */

interface SharedPrefsHelper {

    fun getLastShowChangelogVersion(): Int
    fun setLastShowChangelogVersion(versionCode: Int)

    fun saveLastCoinsUpdateTime(lastUpdateTime: Long)
    fun getLastCoinsUpdateTime(): Long

    fun setShowPortfolioAtHome(showPortfolioAtHome: Boolean)
    fun getShowPortfolioAtHome(): Boolean

    fun saveCmcGlobalData(data: CmcGlobalDataResponse?)
    fun getCmcGlobalData(): CmcGlobalDataResponse?

    fun saveCmcMarketCapAndVolumeChartData(data: CmcMarketCapAndVolumeChartResponse?)
    fun getCmcMarketCapAndVolumeChartData(): CmcMarketCapAndVolumeChartResponse?
}