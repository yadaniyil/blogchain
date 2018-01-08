package com.yadaniil.blogchain.data.api.models.cryptocompare

import com.google.gson.annotations.SerializedName

/**
 * Created by danielyakovlev on 7/15/17.
 */
class CryptoComparePriceMultiFullResponse(
        @SerializedName("RAW")
        val raw: Map<String, Map<String, CryptoComparePriceMultiFullCurrencyItem>>?,
        @SerializedName("DISPLAY")
        val display: Map<String, Map<String, CryptoComparePriceMultiFullCurrencyItem>>?) {


    class CryptoComparePriceMultiFullCurrencyItem(
            @SerializedName("TYPE") val type: Long,
            @SerializedName("MARKET") val market: String,
            @SerializedName("FROMSYMBOL") val fromSymbol: String,
            @SerializedName("TOSYMBOL") val toSymbol: String,
            @SerializedName("FLAGS") val flags: Long,
            @SerializedName("PRICE") val price: String,
            @SerializedName("LASTUPDATE") val lastUpdate: Long,
            @SerializedName("LASTVOLUME") val lastVolume: String,
            @SerializedName("LASTVOLUMETO") val lastVolumeTo: String,
            @SerializedName("LASTTRADEID") val lastTradeId: String,
            @SerializedName("VOLUME24HOUR") val volume24Hour: String,
            @SerializedName("VOLUME24HOURTO") val volume24HourTo: String,
            @SerializedName("OPEN24HOUR") val open24Hour: String,
            @SerializedName("HIGH24HOUR") val high24Hour: String,
            @SerializedName("LOW24HOUR") val low24Hour: String,
            @SerializedName("LASTMARKET") val lastMarket: String,
            @SerializedName("CHANGE24HOUR") val change24Hour: String,
            @SerializedName("CHANGEPCT24HOUR") val changePct24Hour: String,
            @SerializedName("SUPPLY") val supply: String,
            @SerializedName("MKTCAP") val marketCap: String
    )
}
