package com.yadaniil.app.cryptomarket.data.api

import com.yadaniil.app.cryptomarket.data.api.models.TickerResponse
import com.yadaniil.app.cryptomarket.utils.Endpoints
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by danielyakovlev on 7/1/17.
 */
interface CoinMarketCapService {

    @GET(Endpoints.COIN_MARKET_CAP_TICKER_ENDPOINT)
    fun getAllCurrencies(@Query("convert") convertToCurrency: String? = null,
                         @Query("limit") limit: String? = null): Observable<List<TickerResponse>>
}