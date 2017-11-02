package com.yadaniil.blogchain.data

import com.yadaniil.blogchain.data.api.*
import com.yadaniil.blogchain.data.api.models.*
import com.yadaniil.blogchain.data.db.AppDbHelper
import com.yadaniil.blogchain.data.db.DbHelper
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.data.prefs.SharedPrefs
import com.yadaniil.blogchain.data.prefs.SharedPrefsHelper
import io.reactivex.Observable
import io.realm.RealmResults
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
    override fun getAllCoinMarketCapCurrenciesFromDb(): RealmResults<CoinMarketCapCurrencyRealm> =
            appDbHelper.getAllCoinMarketCapCurrenciesFromDb()

    override fun getAllCoinMarketCapCurrenciesFromDbFiltered(text: String): RealmResults<CoinMarketCapCurrencyRealm> =
            appDbHelper.getAllCoinMarketCapCurrenciesFromDbFiltered(text)

    override fun saveCoinMarketCapCurrenciesToDb(currencies: List<CoinMarketCapCurrencyRealm>) =
        appDbHelper.saveCoinMarketCapCurrenciesToDb(currencies)

    override fun getAllCryptoCompareCurrenciesFromDb(): RealmResults<CryptoCompareCurrencyRealm> =
            appDbHelper.getAllCryptoCompareCurrenciesFromDb()

    override fun saveCryptoCompareCurrenciesToDb(currencies: List<CryptoCompareCurrencyRealm>) =
        appDbHelper.saveCryptoCompareCurrenciesToDb(currencies)

    override fun saveCryptoCompareCurrencyIcon(currency: CoinMarketCapCurrencyRealm, byteArray: ByteArray) =
        appDbHelper.saveCryptoCompareCurrencyIcon(currency, byteArray)

    override fun addCurrencyToFavourite(currency: CoinMarketCapCurrencyRealm) =
            appDbHelper.addCurrencyToFavourite(currency)

    override fun getAllFavouriteCurrencies(): RealmResults<CoinMarketCapCurrencyRealm> =
            appDbHelper.getAllFavouriteCurrencies()

    override fun getCMCCurrencyFromDb(symbol: String) = appDbHelper.getCMCCurrencyFromDb(symbol)
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
                             hardwareCost: String?): Observable<MiningCoinResponse> {
        return appApiHelper.getCoinById(coinId, userHashrate, power, poolFeePercent, electricityCost, hardwareCost)
    }
    // endregion Api

    // region SharedPrefs
    override fun getLastShowChangelogVersion() = sharedPrefs.getLastShowChangelogVersion()

    override fun setLastShowChangelogVersion(versionCode: Int)
            = sharedPrefs.setLastShowChangelogVersion(versionCode)
    // endregion SharedPrefs
}