package com.yadaniil.blogchain.data.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 1/5/18.
 */


class CmcGlobalDataResponse(
        @SerializedName("total_market_cap_usd") var totalMarketCapUsd: Double,
        @SerializedName("total_24h_volume_usd") var total24hVolumeUsd: Double,
        @SerializedName("bitcoin_percentage_of_market_cap") var bitcoinDominance: Double,
        @SerializedName("active_currencies") var activeCurrencies: Int,
        @SerializedName("active_assets") var activeAssets: Int,
        @SerializedName("active_markets") var activeMarkets: Int,
        @SerializedName("last_updated") var lastUpdated: Long,
        var totalMarketCapAnalogue: Double = 0.0,
        var total24hVolumeAnalogue: Double = 0.0
)