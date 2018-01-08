package com.yadaniil.blogchain.data.api.models.coinmarketcap

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 1/7/18.
 */

class CmcMarketCapAndVolumeChartResponse(
        @SerializedName("market_cap_by_available_supply")
        var marketCaps: List<List<Long>>,
        @SerializedName("volume_usd")
        var volumes: List<List<Long>>) {

    inner class MarketCapChartMomentData(val timestamp: Long, val capitalisationUsd: Double)
    inner class VolumeChartMomentData(val timestamp: Long, val volumeUsd: Double)
}