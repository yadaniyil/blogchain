package com.yadaniil.blogchain.utils

import com.yadaniil.blogchain.data.api.models.coinmarketcap.TickerResponse
import com.yadaniil.blogchain.data.api.models.cryptocompare.CryptoCompareCurrenciesListResponse
import com.yadaniil.blogchain.data.db.models.CoinEntity
import timber.log.Timber

/**
 * Created by danielyakovlev on 3/5/18.
 */

object CoinMapper {

    fun mapCoins(fullUpdateResponse: Pair<CryptoCompareCurrenciesListResponse, List<TickerResponse>>,
                 allCoinsFromDb: List<CoinEntity>): List<CoinEntity> {
        val cryptoCompareCoins = fullUpdateResponse.first.data?.values?.toList() ?: emptyList()
        val coinMarketCapCoins = fullUpdateResponse.second
        val coinEntities: MutableList<CoinEntity> = ArrayList()

        for (coin in coinMarketCapCoins) {
            try {
                val ccCoin = cryptoCompareCoins.find { it.symbol == coin.symbol }
                val oldEntity = allCoinsFromDb.find { it.symbol == coin.symbol }
                var updatedEntity: CoinEntity
                if (oldEntity == null) {
                    updatedEntity = CoinEntity(
                            cmcId = coin.id,
                            name = coin.name,
                            symbol = coin.symbol,
                            rank = coin.rank,
                            priceUsd = toSafeDouble(coin.priceUsd),
                            priceBtc = toSafeDouble(coin.priceBtc),
                            volume24hUsd = toSafeDouble(coin.volume24hUsd),
                            marketCapUsd = toSafeDouble(coin.marketCapUsd),
                            availableSupply = toSafeDouble(coin.availableSupply),
                            totalSupply = toSafeDouble(coin.totalSupply),
                            maxSupply = toSafeDouble(coin.maxSupply),
                            percentChange1h = toSafeDouble(coin.percentChange1h),
                            percentChange24h = toSafeDouble(coin.percentChange24h),
                            percentChange7d = toSafeDouble(coin.percentChange7d),
                            lastUpdated = coin.lastUpdated,
                            iconBytes = null,
                            isFavourite = oldEntity?.isFavourite ?: false,
                            priceFiatAnalogue = toSafeDouble(coin.priceFiatAnalogue),
                            volume24hFiatAnalogue = toSafeDouble(coin.dayVolumeFiatAnalogue),
                            marketCapFiatAnalogue = toSafeDouble(coin.marketCapFiatAnalogue),
                            ccUrl = ccCoin?.url ?: "",
                            ccImageUrl = ccCoin?.imageUrl ?: "",
                            ccName = ccCoin?.name ?: "",
                            ccCoinName = ccCoin?.coinName ?: "",
                            ccFullName = ccCoin?.fullName ?: "",
                            algorithm = ccCoin?.algorithm ?: "",
                            proofType = ccCoin?.proofType ?: "",
                            fullyPremined = ccCoin?.fullyPremined ?: "",
                            totalCoinSupply = ccCoin?.totalCoinSupply ?: "",
                            preminedValue = ccCoin?.preminedValue ?: "",
                            totalCoinsFreeFloat = ccCoin?.totalCoinsFreeFloat ?: "",
                            sortOrder = ccCoin?.sortOrder ?: 0)
                } else {
                    updatedEntity = oldEntity.copy(
                            cmcId = coin.id,
                            name = coin.name,
                            symbol = coin.symbol,
                            rank = coin.rank,
                            priceUsd = toSafeDouble(coin.priceUsd),
                            priceBtc = toSafeDouble(coin.priceBtc),
                            volume24hUsd = toSafeDouble(coin.volume24hUsd),
                            marketCapUsd = toSafeDouble(coin.marketCapUsd),
                            availableSupply = toSafeDouble(coin.availableSupply),
                            totalSupply = toSafeDouble(coin.totalSupply),
                            maxSupply = toSafeDouble(coin.maxSupply),
                            percentChange1h = toSafeDouble(coin.percentChange1h),
                            percentChange24h = toSafeDouble(coin.percentChange24h),
                            percentChange7d = toSafeDouble(coin.percentChange7d),
                            lastUpdated = coin.lastUpdated,
                            iconBytes = null,
                            priceFiatAnalogue = toSafeDouble(coin.priceFiatAnalogue),
                            volume24hFiatAnalogue = toSafeDouble(coin.dayVolumeFiatAnalogue),
                            marketCapFiatAnalogue = toSafeDouble(coin.marketCapFiatAnalogue),
                            ccUrl = ccCoin?.url ?: "",
                            ccImageUrl = ccCoin?.imageUrl ?: "",
                            ccName = ccCoin?.name ?: "",
                            ccCoinName = ccCoin?.coinName ?: "",
                            ccFullName = ccCoin?.fullName ?: "",
                            algorithm = ccCoin?.algorithm ?: "",
                            proofType = ccCoin?.proofType ?: "",
                            fullyPremined = ccCoin?.fullyPremined ?: "",
                            totalCoinSupply = ccCoin?.totalCoinSupply ?: "",
                            preminedValue = ccCoin?.preminedValue ?: "",
                            totalCoinsFreeFloat = ccCoin?.totalCoinsFreeFloat ?: "",
                            sortOrder = ccCoin?.sortOrder ?: 0)

                }
                coinEntities.add(updatedEntity)
            } catch (e: Exception) {
                Timber.e(e.message)
            }
        }
        return coinEntities
    }

    private fun toSafeDouble(string: String) = try {
        string.toDouble()
    } catch (e: Exception) {
        0.0
    }


}