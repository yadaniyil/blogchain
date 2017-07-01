package com.yadaniil.app.cryptomarket.data.api

import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.api.models.TickerResponse
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by danielyakovlev on 7/1/17.
 */
class AppApiHelper : RetrofitService {

    @Inject lateinit var retrofitService: RetrofitService

    init { Application.component?.inject(this) }

    override fun getAllCurrencies(convertToCurrency: String?, limit: String?)
            : Observable<List<TickerResponse>> {
        return retrofitService.getAllCurrencies(convertToCurrency, limit)
    }

}