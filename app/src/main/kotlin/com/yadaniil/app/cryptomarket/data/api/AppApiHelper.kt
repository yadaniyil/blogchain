package com.yadaniil.app.cryptomarket.data.api

import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.api.models.CryptoCompareCurrenciesListResponse
import com.yadaniil.app.cryptomarket.data.api.models.TickerResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppApiHelper : CoinMarketCapService, CryptoCompareService, CryptoCompareMinService {

    @Inject lateinit var coinMarketCapService: CoinMarketCapService
    @Inject lateinit var cryptoCompareService: CryptoCompareService
    @Inject lateinit var cryptoCompareMinService: CryptoCompareMinService

    init {
        Application.component?.inject(this)
    }

    override fun getAllCurrencies(convertToCurrency: String?, limit: String?)
            : Observable<List<TickerResponse>> =
            coinMarketCapService.getAllCurrencies(convertToCurrency, limit)

    override fun getFullCurrenciesList(): Observable<CryptoCompareCurrenciesListResponse> =
            cryptoCompareService.getFullCurrenciesList()

    override fun getPriceMultiFull(fromSymbols: String, toSymbols: String,
                                   exchangeName: String?, appName: String?,
                                   serverSignRequests: Boolean?, tryConversion: String?) =
            cryptoCompareMinService.getPriceMultiFull(fromSymbols, toSymbols, exchangeName, appName,
                    serverSignRequests, tryConversion)

}