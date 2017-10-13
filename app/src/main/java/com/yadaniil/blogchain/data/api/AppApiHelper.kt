package com.yadaniil.blogchain.data.api

import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.api.models.CryptoCompareCurrenciesListResponse
import com.yadaniil.blogchain.data.api.models.MiningCoinsResponse
import com.yadaniil.blogchain.data.api.models.MinersResponse
import io.reactivex.Observable
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
}