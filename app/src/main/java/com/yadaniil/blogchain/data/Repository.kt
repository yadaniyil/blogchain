package com.yadaniil.blogchain.data

import com.yadaniil.blogchain.data.api.*
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcGlobalDataResponse
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcMarketCapAndVolumeChartResponse
import com.yadaniil.blogchain.data.api.models.coinmarketcap.TickerResponse
import com.yadaniil.blogchain.data.api.models.cryptocompare.CryptoCompareCurrenciesListResponse
import com.yadaniil.blogchain.data.api.models.cryptocompare.CryptoComparePriceMultiFullResponse
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoinsResponse
import com.yadaniil.blogchain.data.api.services.*
import com.yadaniil.blogchain.data.db.AppDbHelper
import com.yadaniil.blogchain.data.db.DbHelper
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import com.yadaniil.blogchain.data.prefs.SharedPrefs
import com.yadaniil.blogchain.data.prefs.SharedPrefsHelper
import io.objectbox.android.ObjectBoxLiveData
import io.reactivex.Observable

/**
 * Created by danielyakovlev on 7/1/17.
 */

class Repository(private var api: AppApiHelper,
                 private var db: AppDbHelper,
                 var sharedPrefs: SharedPrefs)
    : CoinMarketCapService, CryptoCompareService, CryptoCompareMinService, WhatToMineService,
        DbHelper, SharedPrefsHelper, CoinMarketCapGraphsService, CoindarService {

    // region Db
    override fun getAllCoinsFromDb(): List<CoinEntity> = db.getAllCoinsFromDb()

    override fun getAllCoinsFromDbLiveData(): ObjectBoxLiveData<CoinEntity> = db.getAllCoinsFromDbLiveData()

    override fun getAllCoinsFiltered(text: String): List<CoinEntity> = db.getAllCoinsFiltered(text)
    override fun getAllCoinsFilteredLiveData(text: String): ObjectBoxLiveData<CoinEntity> = getAllCoinsFilteredLiveData(text)

    override fun getAllCoinsSorted(fieldName: String, isDescending: Boolean): List<CoinEntity> =
            db.getAllCoinsSorted(fieldName, isDescending)

    override fun getAllCoinsSortedLiveData(fieldName: String, isDescending: Boolean) =
            db.getAllCoinsSortedLiveData(fieldName, isDescending)

    override fun saveCoinsToDb(coins: List<CoinEntity>) {
        db.saveCoinsToDb(coins)
    }

    override fun getCoinFromDb(symbol: String): CoinEntity? = db.getCoinFromDb(symbol)

    override fun addCoinToFavourite(coin: CoinEntity) {
        db.addCoinToFavourite(coin)
    }

    override fun removeCoinFromFavourites(coin: CoinEntity) {
        db.removeCoinFromFavourites(coin)
    }

    override fun getAllFavouriteCoins(): List<CoinEntity> = db.getAllFavouriteCoins()

    override fun getAllFavouriteCoinsLiveData(): ObjectBoxLiveData<CoinEntity> = db.getAllFavouriteCoinsLiveData()

    override fun getFavouriteCoinsFiltered(text: String): List<CoinEntity> = db.getFavouriteCoinsFiltered(text)

    override fun getFavouriteCoinsFilteredLiveData(text: String): ObjectBoxLiveData<CoinEntity> =
            db.getFavouriteCoinsFilteredLiveData(text)

    override fun savePortfolioCoin(portfolioCoinEntity: PortfolioCoinEntity) =
            db.savePortfolioCoin(portfolioCoinEntity)

    override fun getAllPortfolioCoins(): List<PortfolioCoinEntity> = db.getAllPortfolioCoins()

    override fun getAllPortfolioCoinsLiveData(): ObjectBoxLiveData<PortfolioCoinEntity> =
            db.getAllPortfolioCoinsLiveData()

    override fun getSinglePortfolioCoin(portfolioCoinEntityId: Long): PortfolioCoinEntity? =
            db.getSinglePortfolioCoin(portfolioCoinEntityId)

    override fun removePortfolioCoin(portfolioCoinEntityId: Long) =
            db.removePortfolioCoin(portfolioCoinEntityId)

    // endregion Db

    // region Api
    override fun getAllCoins(convertToCurrency: String?, limit: String?): Observable<List<TickerResponse>> =
            api.getAllCoins(convertToCurrency, limit)

    override fun getCoin(coinId: String, convertToCurrency: String?): Observable<String> =
            api.getCoin(coinId, convertToCurrency)

    override fun getFullCurrenciesList(): Observable<CryptoCompareCurrenciesListResponse> =
            api.getFullCurrenciesList()

    override fun getPriceMultiFull(fromSymbols: String, toSymbols: String, exchangeName: String?,
                                   appName: String?, serverSignRequests: Boolean?,
                                   tryConversion: String?): Observable<CryptoComparePriceMultiFullResponse> =
            api.getPriceMultiFull(fromSymbols, toSymbols, exchangeName, appName,
                    serverSignRequests, tryConversion)

    override fun getMiners() = api.getMiners()

    override fun getAllGpuMiningCoins(): Observable<MiningCoinsResponse> = api.getAllGpuMiningCoins()

    override fun getAllAsicMiningCoins(): Observable<MiningCoinsResponse> = api.getAllAsicMiningCoins()

    override fun getMiningCoinById(coinId: String, userHashrate: String?, power: String?,
                                   poolFeePercent: String?, electricityCost: String?,
                                   hardwareCost: String?) =
            api.getMiningCoinById(coinId, userHashrate, power, poolFeePercent, electricityCost, hardwareCost)

    override fun getGlobalData(convertToCurrency: String?) = api.getGlobalData(convertToCurrency)

    override fun downloadCmcMarketCapAndVolumeCharts() = api.downloadCmcMarketCapAndVolumeCharts()

    override fun downloadAllEvents() = api.downloadAllEvents()
    // endregion Api

    // region SharedPrefs
    override fun getLastShowChangelogVersion() = sharedPrefs.getLastShowChangelogVersion()

    override fun setLastShowChangelogVersion(versionCode: Int) = sharedPrefs.setLastShowChangelogVersion(versionCode)

    override fun saveLastCoinsUpdateTime(lastUpdateTime: Long) = sharedPrefs.saveLastCoinsUpdateTime(lastUpdateTime)
    override fun getLastCoinsUpdateTime() = sharedPrefs.getLastCoinsUpdateTime()

    override fun setShowPortfolioAtHome(showPortfolioAtHome: Boolean) = sharedPrefs.setShowPortfolioAtHome(showPortfolioAtHome)
    override fun getShowPortfolioAtHome() = sharedPrefs.getShowPortfolioAtHome()

    override fun saveCmcGlobalData(data: CmcGlobalDataResponse?) = sharedPrefs.saveCmcGlobalData(data)
    override fun getCmcGlobalData(): CmcGlobalDataResponse? = sharedPrefs.getCmcGlobalData()

    override fun saveCmcMarketCapAndVolumeChartData(data: CmcMarketCapAndVolumeChartResponse?) = sharedPrefs.saveCmcMarketCapAndVolumeChartData(data)
    override fun getCmcMarketCapAndVolumeChartData() = sharedPrefs.getCmcMarketCapAndVolumeChartData()
    // endregion SharedPrefs
}