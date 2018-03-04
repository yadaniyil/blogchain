package com.yadaniil.blogchain.data.api

import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.api.models.coindar.CoindarEventResponse
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcMarketCapAndVolumeChartResponse
import com.yadaniil.blogchain.data.api.models.cryptocompare.CryptoCompareCurrenciesListResponse
import com.yadaniil.blogchain.data.api.models.cryptocompare.MinersResponse
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoinResponse
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoinsResponse
import com.yadaniil.blogchain.data.api.services.*
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppApiHelper : CoinMarketCapService, CryptoCompareService,
        CryptoCompareMinService, WhatToMineService, CoinMarketCapGraphsService
        , CoindarService {

    @Inject lateinit var coinMarketCapService: CoinMarketCapService
    @Inject lateinit var cryptoCompareService: CryptoCompareService
    @Inject lateinit var cryptoCompareMinService: CryptoCompareMinService
    @Inject lateinit var whatToMineService: WhatToMineService
    @Inject lateinit var coinMarketCapGraphsService: CoinMarketCapGraphsService
    @Inject lateinit var coindarService: CoindarService

    init {
        Application.component?.inject(this)
    }

    override fun getAllCoins(convertToCurrency: String?, limit: String?) =
            coinMarketCapService.getAllCoins(convertToCurrency, limit)

    override fun getCoin(coinId: String, convertToCurrency: String?): Observable<String> =
            coinMarketCapService.getCoin(coinId, convertToCurrency)


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

    override fun getMiningCoinById(coinId: String, userHashrate: String?, power: String?,
                                   poolFeePercent: String?, electricityCost: String?,
                                   hardwareCost: String?): Observable<MiningCoinResponse> {
        return whatToMineService.getMiningCoinById(coinId, userHashrate, power, poolFeePercent, electricityCost, hardwareCost)
    }

    override fun getGlobalData(convertToCurrency: String?) = coinMarketCapService.getGlobalData(convertToCurrency)

    override fun downloadCmcMarketCapAndVolumeCharts() = coinMarketCapGraphsService.downloadCmcMarketCapAndVolumeCharts()

    override fun downloadAllEvents() = coindarService.downloadAllEvents()
}