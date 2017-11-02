package com.yadaniil.blogchain.data.db

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface DbHelper {

    fun getAllCoinMarketCapCoinsFromDb(): RealmResults<CoinMarketCapCurrencyRealm>
    fun getAllCoinMarketCapCoinsFromDbFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm>
    fun saveCoinMarketCapCoinsToDb(coins: List<CoinMarketCapCurrencyRealm>)
    fun getCMCCoinFromDb(symbol: String): CoinMarketCapCurrencyRealm

    fun getAllCryptoCompareCoinsFromDb(): RealmResults<CryptoCompareCurrencyRealm>
    fun saveCryptoCompareCoinsToDb(coins: List<CryptoCompareCurrencyRealm>)
    fun saveCryptoCompareCoinIcon(coin: CoinMarketCapCurrencyRealm, byteArray: ByteArray)

    fun addCoinToFavourite(coin: CoinMarketCapCurrencyRealm)
    fun removeCoinFromFavourites(coin: CoinMarketCapCurrencyRealm)
    fun getAllFavouriteCoins(): RealmResults<CoinMarketCapCurrencyRealm>
}