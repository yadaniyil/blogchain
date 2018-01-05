package com.yadaniil.blogchain.data.api

import com.yadaniil.blogchain.data.api.models.CmcGlobalDataResponse
import com.yadaniil.blogchain.data.api.models.TickerResponse
import com.yadaniil.blogchain.utils.Endpoints
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface CoinMarketCapService {

    @GET(Endpoints.COIN_MARKET_CAP_TICKER_ENDPOINT)
    fun getAllCoins(@Query("convert") convertToCurrency: String? = null,
                    @Query("limit") limit: String? = null): Observable<List<TickerResponse>>

    @GET("ticker/{coinId}")
    fun getCoin(@Path("coinId") coinId: String,
                @Query("convert") convertToCurrency: String? = null): Observable<String>

    @GET(Endpoints.COIN_MARKET_CAP_GLOBAL_DATA_ENDPOINT)
    fun getGlobalData(@Query("convert") convertToCurrency: String? = null): Observable<CmcGlobalDataResponse>
}