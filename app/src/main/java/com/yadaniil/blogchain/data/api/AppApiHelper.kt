package com.yadaniil.blogchain.data.api

import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.api.models.*
import io.reactivex.Observable
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppApiHelper : CoinMarketCapService, CryptoCompareService,
        CryptoCompareMinService, WhatToMineService {

    @Inject lateinit var coinMarketCapService: CoinMarketCapService
    @Inject lateinit var cryptoCompareService: CryptoCompareService
    @Inject lateinit var cryptoCompareMinService: CryptoCompareMinService
    @Inject lateinit var whatToMineService: WhatToMineService

    init {
        Application.component?.inject(this)
    }

    override fun getAllCurrencies(convertToCurrency: String?, limit: String?) =
            coinMarketCapService.getAllCurrencies(convertToCurrency, limit)

    override fun getFullCurrenciesList(): Observable<CryptoCompareCurrenciesListResponse> =
            cryptoCompareService.getFullCurrenciesList()

    override fun getPriceMultiFull(fromSymbols: String, toSymbols: String,
                                   exchangeName: String?, appName: String?,
                                   serverSignRequests: Boolean?, tryConversion: String?) =
            cryptoCompareMinService.getPriceMultiFull(fromSymbols, toSymbols, exchangeName, appName,
                    serverSignRequests, tryConversion)

    override fun getMiners(): Observable<MinersResponse> = cryptoCompareService.getMiners()

    override fun getAllGpuMiningCoins(): Observable<MiningCoinsResponse> = whatToMineService.getAllGpuMiningCoins()

    override fun getAllAsicMiningCoins(): Observable<MiningCoinsResponse> = whatToMineService.getAllAsicMiningCoins()

    override fun getCoinById(coinId: String, userHashrate: String?, power: String?,
                             poolFeePercent: String?, electricityCost: String?,
                             hardwareCost: String?): Observable<MiningCoinResponse> {
        return whatToMineService.getCoinById(coinId, userHashrate, power, poolFeePercent, electricityCost, hardwareCost)
    }

}