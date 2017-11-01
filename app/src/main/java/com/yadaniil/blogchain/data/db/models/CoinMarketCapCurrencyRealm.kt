package com.yadaniil.blogchain.data.db.models

import com.yadaniil.blogchain.data.api.models.TickerResponse
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

/**
 * Created by danielyakovlev on 7/1/17.
 */
open class CoinMarketCapCurrencyRealm(
        @PrimaryKey var id: String? = "",
        var name: String? = "",
        var symbol: String? = "",
        var rank: Int = 0,
        var priceUsd: String? = "",
        var priceBtc: String? = "",
        var volume24hUsd: String? = "",
        var marketCapUsd: String? = "",
        var availableSupply: String? = "",
        var totalSupply: String? = "",
        var percentChange1h: String? = "",
        var percentChange24h: String? = "",
        var percentChange7d: String? = "",
        var lastUpdated: Long = 0L,
        var iconBytes: ByteArray? = null,
        var isFavourite: Boolean = false
) : RealmObject() {

    fun getVolumeFormatted() = if (volume24hUsd == null) "0" else volume24hUsd

    companion object {
        fun convertApiResponseToRealmList(tickerResponse: List<TickerResponse>): List<CoinMarketCapCurrencyRealm> {
            val currenciesRealmList: MutableList<CoinMarketCapCurrencyRealm> = ArrayList()
            tickerResponse.forEach {
                currenciesRealmList.add(CoinMarketCapCurrencyRealm(it.id, it.name, it.symbol, it.rank,
                        it.priceUsd, it.priceBtc, it.volume24hUsd, it.marketCapUsd, it.availableSupply,
                        it.totalSupply, it.percentChange1h, it.percentChange24h, it.percentChange7d,
                        it.lastUpdated))
            }
            return currenciesRealmList
        }
    }
}