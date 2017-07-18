package com.yadaniil.app.cryptomarket.data.api

import com.yadaniil.app.cryptomarket.data.api.models.CryptoComparePriceMultiFullResponse
import com.yadaniil.app.cryptomarket.utils.Endpoints
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by danielyakovlev on 7/15/17.
 */
interface CryptoCompareMinService {

    @FormUrlEncoded
    @POST(Endpoints.CRYPTO_COMPARE_PRICE_MULTI_FULL_ENDPOINT)
    fun getPriceMultiFull(@Field("fsyms") fromSymbols: String, @Field("tsyms") toSymbols: String,
                          @Field("e") exchangeName: String? = null,
                          @Field("extraParams") appName: String? = null,
                          @Field("sign") serverSignRequests: Boolean? = null,
                          @Field("tryConversion") tryConversion: String? = null)
            : Observable<CryptoComparePriceMultiFullResponse>
}