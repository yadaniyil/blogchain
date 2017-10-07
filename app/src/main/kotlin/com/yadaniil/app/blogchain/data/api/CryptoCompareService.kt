package com.yadaniil.app.blogchain.data.api

import com.yadaniil.app.blogchain.data.api.models.CryptoCompareCurrenciesListResponse
import com.yadaniil.app.blogchain.data.api.models.MinersResponse
import com.yadaniil.app.blogchain.utils.Endpoints
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by danielyakovlev on 7/2/17.
 */
interface CryptoCompareService {

    @GET(Endpoints.CRYPTO_COMPARE_COIN_LIST_ENDPOINT)
    fun getFullCurrenciesList(): Observable<CryptoCompareCurrenciesListResponse>

    @GET(Endpoints.CRYPTO_COMPARE_MINERS_ENDPOINT)
    fun getMiners(): Observable<MinersResponse>

}