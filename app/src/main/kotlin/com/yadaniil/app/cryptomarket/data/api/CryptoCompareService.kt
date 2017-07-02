package com.yadaniil.app.cryptomarket.data.api

import com.yadaniil.app.cryptomarket.data.api.models.CryptoCompareCurrenciesListResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by danielyakovlev on 7/2/17.
 */
interface CryptoCompareService {

    @GET("api/data/coinlist/")
    fun getFullCurrenciesList(): Observable<CryptoCompareCurrenciesListResponse>
}