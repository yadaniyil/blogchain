package com.yadaniil.blogchain.data.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 7/2/17.
 */
class TickerResponse(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("symbol") val symbol: String,
        @SerializedName("rank") val rank: Int,
        @SerializedName("price_usd") val priceUsd: String,
        @SerializedName("price_btc") val priceBtc: String,
        @SerializedName("24h_volume_usd") val volume24hUsd: String,
        @SerializedName("market_cap_usd") val marketCapUsd: String,
        @SerializedName("available_supply") val availableSupply: String,
        @SerializedName("total_supply") val totalSupply: String,
        @SerializedName("percent_change_1h") val percentChange1h: String,
        @SerializedName("percent_change_24h") val percentChange24h: String,
        @SerializedName("percent_change_7d") val percentChange7d: String,
        @SerializedName("last_updated") val lastUpdated: Long
)