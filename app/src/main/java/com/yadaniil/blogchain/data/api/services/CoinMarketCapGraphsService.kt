package com.yadaniil.blogchain.data.api.services

import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcMarketCapAndVolumeChartResponse
import com.yadaniil.blogchain.utils.Endpoints
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by danielyakovlev on 1/7/18.
 */

interface CoinMarketCapGraphsService {

    @GET(Endpoints.COIN_MARKET_CAP_GRAPHS_MARKETCAP_AND_VOLUME_ENDPOINT)
    fun downloadCmcMarketCapAndVolumeCharts(): Observable<CmcMarketCapAndVolumeChartResponse>
}