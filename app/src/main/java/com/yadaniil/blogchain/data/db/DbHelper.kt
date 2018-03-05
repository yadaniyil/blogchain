package com.yadaniil.blogchain.data.db

import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import io.objectbox.android.ObjectBoxLiveData

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface DbHelper {

    // region Coins
    fun getAllCoinsFromDb(): List<CoinEntity>
    fun getAllCoinsFromDbLiveData(): ObjectBoxLiveData<CoinEntity>

    fun getAllCoinsFiltered(text: String): List<CoinEntity>
    fun getAllCoinsFilteredLiveData(text: String): ObjectBoxLiveData<CoinEntity>

    fun getAllCoinsSorted(fieldName: String, isDescending: Boolean): List<CoinEntity>
    fun getAllCoinsSortedLiveData(fieldName: String, isDescending: Boolean): ObjectBoxLiveData<CoinEntity>

    fun saveCoinsToDb(coins: List<CoinEntity>)
    fun getCoinFromDb(symbol: String): CoinEntity?

    fun addCoinToFavourite(coin: CoinEntity)
    fun removeCoinFromFavourites(coin: CoinEntity)

    fun getAllFavouriteCoins(): List<CoinEntity>
    fun getAllFavouriteCoinsLiveData(): ObjectBoxLiveData<CoinEntity>

    fun getFavouriteCoinsFiltered(text: String): List<CoinEntity>
    fun getFavouriteCoinsFilteredLiveData(text: String): ObjectBoxLiveData<CoinEntity>
    // endregion Coins

    // region Portfolio
    fun savePortfolioCoin(portfolioCoinEntity: PortfolioCoinEntity)

    fun getAllPortfolioCoins(): List<PortfolioCoinEntity>
    fun getAllPortfolioCoinsLiveData(): ObjectBoxLiveData<PortfolioCoinEntity>

    fun getSinglePortfolioCoin(portfolioCoinEntityId: Long): PortfolioCoinEntity?
    fun removePortfolioCoin(portfolioCoinEntityId: Long)
    // endregion Portfolio
}