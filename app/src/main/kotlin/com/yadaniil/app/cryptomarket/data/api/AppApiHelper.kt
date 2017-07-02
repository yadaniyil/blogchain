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
class AppApiHelper : CoinMarketCapService, CryptoCompareService {

    @Inject lateinit var coinMarketCapService: CoinMarketCapService
    @Inject lateinit var cryptoCompareService: CryptoCompareService

    init { Application.component?.inject(this) }

    override fun getAllCurrencies(convertToCurrency: String?, limit: String?)
            : Observable<List<TickerResponse>> {
        return coinMarketCapService.getAllCurrencies(convertToCurrency, limit)
    }

    override fun getFullCurrenciesList(): Observable<CryptoCompareCurrenciesListResponse> {
        return cryptoCompareService.getFullCurrenciesList()
    }

}