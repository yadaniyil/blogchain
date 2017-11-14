package com.yadaniil.blogchain.data

import com.yadaniil.blogchain.data.api.*
import com.yadaniil.blogchain.data.api.models.*
import com.yadaniil.blogchain.data.db.AppDbHelper
import com.yadaniil.blogchain.data.db.DbHelper
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.data.prefs.SharedPrefs
import com.yadaniil.blogchain.data.prefs.SharedPrefsHelper
import io.reactivex.Observable
import io.realm.RealmResults
import io.realm.Sort
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by danielyakovlev on 7/1/17.
 */

@Singleton
class Repository @Inject constructor(private var appApiHelper: AppApiHelper,
                                     private var appDbHelper: AppDbHelper,
                                     var sharedPrefs: SharedPrefs)
    : CoinMarketCapService, CryptoCompareService, CryptoCompareMinService, WhatToMineService,
        DbHelper, SharedPrefsHelper {

    // region Db
    override fun getAllCoinsFromDb(): RealmResults<CoinMarketCapCurrencyRealm> =
            appDbHelper.getAllCoinsFromDb()

    override fun getAllCoinsFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm> =
            appDbHelper.getAllCoinsFiltered(text)

    override fun saveCoinsToDb(coins: List<CoinMarketCapCurrencyRealm>) =
        appDbHelper.saveCoinsToDb(coins)

    override fun getAllCryptoCompareCoinsFromDb(): RealmResults<CryptoCompareCurrencyRealm> =
            appDbHelper.getAllCryptoCompareCoinsFromDb()

    override fun saveCryptoCompareCoinsToDb(coins: List<CryptoCompareCurrencyRealm>) =
        appDbHelper.saveCryptoCompareCoinsToDb(coins)

    override fun saveCryptoCompareCoinIcon(coin: CoinMarketCapCurrencyRealm, byteArray: ByteArray) =
        appDbHelper.saveCryptoCompareCoinIcon(coin, byteArray)

    override fun addCoinToFavourite(coin: CoinMarketCapCurrencyRealm) =
            appDbHelper.addCoinToFavourite(coin)

    override fun removeCoinFromFavourites(coin: CoinMarketCapCurrencyRealm) =
            appDbHelper.removeCoinFromFavourites(coin)

    override fun getAllFavouriteCoins(): RealmResults<CoinMarketCapCurrencyRealm> =
            appDbHelper.getAllFavouriteCoins()

    override fun getCoinFromDb(symbol: String) = appDbHelper.getCoinFromDb(symbol)

    override fun addCoinToPortfolio(coin: CoinMarketCapCurrencyRealm, amountOfCoins: String,
                                    buyPriceOfCoin: String, storageType: String, storageName: String, description: String) =
        appDbHelper.addCoinToPortfolio(coin, amountOfCoins, buyPriceOfCoin, storageType, storageName, description)

    override fun getAllPortfolio() = appDbHelper.getAllPortfolio()

    override fun getAllCoinsSorted(fieldName: String, sortOrder: Sort) =
            appDbHelper.getAllCoinsSorted(fieldName, sortOrder)

    override fun getSinglePortfolio(portfolioId: String) = appDbHelper.getSinglePortfolio(portfolioId)

    override fun editPortfolio(portfolioItem: PortfolioRealm, coin: CoinMarketCapCurrencyRealm,
                               amountOfCoins: String, buyPriceOfCoin: String,
                               storageType: String, storageName: String, description: String)
            = appDbHelper.editPortfolio(portfolioItem, coin, amountOfCoins,
            buyPriceOfCoin, storageType, storageName, description)

    override fun removeItemFromPortfolio(id: String) = appDbHelper.removeItemFromPortfolio(id)
    // endregion Db

    // region Api
    override fun getAllCurrencies(convertToCurrency: String?, limit: String?): Observable<List<TickerResponse>> =
            appApiHelper.getAllCurrencies(convertToCurrency, limit)

    override fun getFullCurrenciesList(): Observable<CryptoCompareCurrenciesListResponse> =
            appApiHelper.getFullCurrenciesList()

    override fun getPriceMultiFull(fromSymbols: String, toSymbols: String, exchangeName: String?,
                                   appName: String?, serverSignRequests: Boolean?,
                                   tryConversion: String?): Observable<CryptoComparePriceMultiFullResponse> =
            appApiHelper.getPriceMultiFull(fromSymbols, toSymbols, exchangeName, appName,
                    serverSignRequests, tryConversion)

    override fun getMiners() = appApiHelper.getMiners()

    override fun getAllGpuMiningCoins(): Observable<MiningCoinsResponse> = appApiHelper.getAllGpuMiningCoins()

    override fun getAllAsicMiningCoins(): Observable<MiningCoinsResponse> = appApiHelper.getAllAsicMiningCoins()

    override fun getCoinById(coinId: String, userHashrate: String?, power: String?,
                             poolFeePercent: String?, electricityCost: String?,
                             hardwareCost: String?) =
            appApiHelper.getCoinById(coinId, userHashrate, power, poolFeePercent, electricityCost, hardwareCost)
    // endregion Api

    // region SharedPrefs
    override fun getLastShowChangelogVersion() = sharedPrefs.getLastShowChangelogVersion()

    override fun setLastShowChangelogVersion(versionCode: Int)
            = sharedPrefs.setLastShowChangelogVersion(versionCode)
    // endregion SharedPrefs
}