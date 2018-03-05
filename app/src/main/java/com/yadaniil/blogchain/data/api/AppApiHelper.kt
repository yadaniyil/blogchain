package com.yadaniil.blogchain.data.api

import com.yadaniil.blogchain.data.api.models.cryptocompare.CryptoCompareCurrenciesListResponse
import com.yadaniil.blogchain.data.api.models.cryptocompare.MinersResponse
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoinResponse
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoinsResponse
import com.yadaniil.blogchain.data.api.services.*
import io.reactivex.Observable

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppApiHelper(private val coinMarketCapService: CoinMarketCapService,
                   private val cryptoCompareService: CryptoCompareService,
                   private val cryptoCompareMinService: CryptoCompareMinService,
                   private val whatToMineService: WhatToMineService,
                   private val coinMarketCapGraphsService: CoinMarketCapGraphsService,
                   private val coindarService: CoindarService)
    : CoinMarketCapService, CryptoCompareService, CryptoCompareMinService,
        WhatToMineService, CoinMarketCapGraphsService, CoindarService {

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