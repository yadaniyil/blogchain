package com.yadaniil.blogchain.utils

import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.screens.allcoins.AllCoinsViewModel

/**
 * Created by danielyakovlev on 3/5/18.
 */

object CoinMapper {

    fun mapCoins(fullUpdateResponse: AllCoinsViewModel.FullUpdateResponse,
                 allCoinsFromDb: List<CoinEntity>): List<CoinEntity> {
        val ccCoins = fullUpdateResponse.ccAllCoinsResponse.data?.values?.toList() ?: emptyList()
        val cmcCoins = fullUpdateResponse.cmcAllCoinsResponse
        val coinEntities: MutableList<CoinEntity> = ArrayList()

        cmcCoins.forEach {
            val ccCoin = ccCoins.find { it.symbol == it.symbol }
            val oldEntity = allCoinsFromDb.find { it.symbol == it.symbol }
            val updatedEntity = oldEntity?.copy(
                    cmcId = it.id,
                    name = it.name,
                    symbol = it.symbol,
                    rank = it.rank,
                    priceUsd = it.priceUsd.toDouble(),
                    priceBtc = it.priceBtc.toDouble(),
                    volume24hUsd = it.volume24hUsd.toDouble(),
                    marketCapUsd = it.marketCapUsd.toDouble(),
                    availableSupply = it.availableSupply.toDouble(),
                    totalSupply = it.totalSupply.toDouble(),
                    percentChange1h = it.percentChange1h.toDouble(),
                    percentChange24h = it.percentChange24h.toDouble(),
                    percentChange7d = it.percentChange7d.toDouble(),
                    lastUpdated = it.lastUpdated,
                    iconBytes = null,
                    isFavourite = oldEntity?.isFavourite ?: false,
                    priceFiatAnalogue = it.priceFiatAnalogue.toDouble(),
                    volume24hFiatAnalogue = it.dayVolumeFiatAnalogue.toDouble(),
                    marketCapFiatAnalogue = it.marketCapFiatAnalogue.toDouble(),
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

            updatedEntity?.let { coinEntities.add(it) }
        }
        return coinEntities
    }
}