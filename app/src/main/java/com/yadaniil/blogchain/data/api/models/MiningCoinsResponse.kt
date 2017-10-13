package com.yadaniil.blogchain.data.api.models

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 9/28/17.
 */


class MiningCoinsResponse(@SerializedName("coins") val miningCoins: Map<String, MiningCoin>)

class MiningCoin(@SerializedName("id") val id: Int,
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
                 @SerializedName("estimated_rewards") val estimatedRewards: String,
                 @SerializedName("estimated_rewards24") val estimatedRewards24: String,
                 @SerializedName("btc_revenue") val btcRevenue: String,
                 @SerializedName("btc_revenue24") val btcRevenue24: String,
                 @SerializedName("profitability") val profitability: Int,
                 @SerializedName("profitability24") val profitability24: Int,
                 @SerializedName("lagging") val isLagging: Boolean,
                 @SerializedName("timestamp") val timestamp: Long,
                 var name: String, var equipmentType: String
)