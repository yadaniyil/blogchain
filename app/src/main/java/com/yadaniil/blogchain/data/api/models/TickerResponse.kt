package com.yadaniil.blogchain.data.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 7/2/17.
 */
class TickerResponse(
        @SerializedName("id") var id: String? = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("symbol") var symbol: String = "",
        @SerializedName("rank") var rank: Int = 0,
        @SerializedName("price_usd") var priceUsd: String = "",
        @SerializedName("price_btc") var priceBtc: String = "",
        @SerializedName("24h_volume_usd") var volume24hUsd: String = "",
        @SerializedName("market_cap_usd") var marketCapUsd: String = "",
        @SerializedName("available_supply") var availableSupply: String = "",
        @SerializedName("total_supply") var totalSupply: String = "",
        @SerializedName("percent_change_1h") var percentChange1h: String = "",
        @SerializedName("percent_change_24h") var percentChange24h: String = "",
        @SerializedName("percent_change_7d") var percentChange7d: String = "",
        @SerializedName("last_updated") var lastUpdated: Long = 0L,
        @SerializedName("max_supply") var maxSupply: String = "",
        var priceFiatAnalogue: String = "",
        var dayVolumeFiatAnalogue: String = "",
        var marketCapFiatAnalogue: String = "")