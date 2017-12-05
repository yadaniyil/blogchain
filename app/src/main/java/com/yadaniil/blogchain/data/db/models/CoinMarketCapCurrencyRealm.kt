package com.yadaniil.blogchain.data.db.models

import com.yadaniil.blogchain.data.api.models.TickerResponse
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by danielyakovlev on 7/1/17.
 */
open class CoinMarketCapCurrencyRealm(
        @PrimaryKey var id: String? = "",
        var name: String? = "",
        var symbol: String? = "",
        var rank: Int = 0,
        var priceUsd: Double = 0.0,
        var priceBtc: Double = 0.0,
        var volume24hUsd: Double = 0.0,
        var marketCapUsd: Double = 0.0,
        var availableSupply: Double = 0.0,
        var totalSupply: Double = 0.0,
        var percentChange1h: Double = 0.0,
        var percentChange24h: Double = 0.0,
        var percentChange7d: Double = 0.0,
        var lastUpdated: Long = 0L,
        var iconBytes: ByteArray? = null,
        var isFavourite: Boolean = false,
        var priceFiatAnalogue: Double = 0.0,
        var volume24hFiatAnalogue: Double = 0.0,
        var marketCapFiatAnalogue: Double = 0.0
) : RealmObject() {

    companion object {
        private fun toDoubleSafe(string: String?) = string?.toDouble() ?: 0.0

        fun convertApiResponseToRealmList(tickerResponse: List<TickerResponse>): List<CoinMarketCapCurrencyRealm> {
            val currenciesRealmList: MutableList<CoinMarketCapCurrencyRealm> = ArrayList()
            tickerResponse.forEach {
                currenciesRealmList.add(CoinMarketCapCurrencyRealm(it.id, it.name, it.symbol, it.rank,
                        toDoubleSafe(it.priceUsd), toDoubleSafe(it.priceBtc),
                        toDoubleSafe(it.volume24hUsd), toDoubleSafe(it.marketCapUsd),
                        toDoubleSafe(it.availableSupply), toDoubleSafe(it.totalSupply),
                        toDoubleSafe(it.percentChange1h), toDoubleSafe(it.percentChange24h),
                        toDoubleSafe(it.percentChange7d), it.lastUpdated))
            }
            return currenciesRealmList
        }

        fun convertSingleTickerToRealmModel(ticker: TickerResponse): CoinMarketCapCurrencyRealm {
            return CoinMarketCapCurrencyRealm(ticker.id, ticker.name, ticker.symbol, ticker.rank,
                    toDoubleSafe(ticker.priceUsd), toDoubleSafe(ticker.priceBtc),
                    toDoubleSafe(ticker.volume24hUsd), toDoubleSafe(ticker.marketCapUsd),
                    toDoubleSafe(ticker.availableSupply), toDoubleSafe(ticker.totalSupply),
                    toDoubleSafe(ticker.percentChange1h), toDoubleSafe(ticker.percentChange24h),
                    toDoubleSafe(ticker.percentChange7d), ticker.lastUpdated, null,
                    false, toDoubleSafe(ticker.priceFiatAnalogue),
                    toDoubleSafe(ticker.dayVolumeFiatAnalogue), toDoubleSafe(ticker.marketCapFiatAnalogue))
        }

    }
}