package com.yadaniil.blogchain.data.db

import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import io.realm.RealmResults
import io.realm.Sort

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface DbHelper {

    // region Coins
    fun getAllCoinsFromDb(): RealmResults<CoinMarketCapCurrencyRealm>
    fun getAllCoinsFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm>
    fun getAllCoinsSorted(fieldName: String, sortOrder: Sort): RealmResults<CoinMarketCapCurrencyRealm>
    fun saveCoinsToDb(coins: List<CoinMarketCapCurrencyRealm>)
    fun getCoinFromDb(symbol: String): CoinMarketCapCurrencyRealm
    fun addCoinToFavourite(coin: CoinMarketCapCurrencyRealm)
    fun removeCoinFromFavourites(coin: CoinMarketCapCurrencyRealm)
    fun getAllFavouriteCoins(): RealmResults<CoinMarketCapCurrencyRealm>
    // endregion Coins

    // region CryptoCompare
    fun getAllCryptoCompareCoinsFromDb(): RealmResults<CryptoCompareCurrencyRealm>
    fun saveCryptoCompareCoinsToDb(coins: List<CryptoCompareCurrencyRealm>)
    fun saveCryptoCompareCoinIcon(coin: CoinMarketCapCurrencyRealm, byteArray: ByteArray)
    // endregion CryptoCompare

    // region Portfolio
    fun addCoinToPortfolio(coin: CoinMarketCapCurrencyRealm, amountOfCoins: String,
                           buyPriceOfCoin: String, storageType: String, storageName: String)
    fun getAllPortfolio(): RealmResults<PortfolioRealm>
    fun getSinglePortfolio(portfolioId: String): PortfolioRealm?
    fun editPortfolio(portfolioItem: PortfolioRealm, coin: CoinMarketCapCurrencyRealm, amountOfCoins: String, buyPriceOfCoin: String, storageType: String, storageName: String)
    fun removeItemFromPortfolio(id: String)
    // endregion Portfolio
}