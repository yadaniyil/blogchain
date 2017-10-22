package com.yadaniil.blogchain.data.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 10/21/17.
 */

class MiningCoinResponse(@SerializedName("id") val id: Int,
                         @SerializedName("name") val name: String,
                         @SerializedName("tag") val tag: String,
                         @SerializedName("algorithm") val algorithm: String,
                         @SerializedName("block_time") val blockTime: String,
                         @SerializedName("block_reward") val blockReward: String,
                         @SerializedName("block_reward24") val blockReward24: String,
                         @SerializedName("last_block") val lastBlock: Long,
                         @SerializedName("difficulty") val difficulty: String,
                         @SerializedName("difficulty24") val difficulty24: String,
                         @SerializedName("nethash") val nethash: String,
                         @SerializedName("exchange_rate") val exchangeRate: String,
                         @SerializedName("exchange_rate24") val exchangeRate24: String,
                         @SerializedName("exchange_rate_vol") val exchangeRateVolume: String,
                         @SerializedName("exchange_rate_curr") val exchangeRateCurrency: String,
                         @SerializedName("market_cap") val marketCap: String,
                         @SerializedName("pool_fee") val pool_fee: String,
                         @SerializedName("estimated_rewards") val estimatedRewards: String,
                         @SerializedName("btc_revenue") val btcRevenue: String,
                         @SerializedName("revenue") val revenueDollar: String,
                         @SerializedName("cost") val cost: String,
                         @SerializedName("profit") val profit: String,
                         @SerializedName("status") val status: String,
                         @SerializedName("lagging") val isLagging: Boolean,
                         @SerializedName("timestamp") val timestamp: Long)