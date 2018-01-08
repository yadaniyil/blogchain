package com.yadaniil.blogchain.data.api.services

import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoinResponse
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoinsResponse
import com.yadaniil.blogchain.utils.Endpoints
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by danielyakovlev on 9/28/17.
 */


interface WhatToMineService {

    @GET(Endpoints.WHAT_TO_MINE_GPU_COINS_ENDPOINT)
    fun getAllGpuMiningCoins(): Observable<MiningCoinsResponse>

    @GET(Endpoints.WHAT_TO_MINE_ASIC_COINS_ENDPOINT)
    fun getAllAsicMiningCoins(): Observable<MiningCoinsResponse>

    @GET("coins/{coinId}.json")
    fun getMiningCoinById(@Path("coinId") coinId: String,
                          @Query("hr") userHashrate: String? = null,
                          @Query("p") power: String? = null,
                          @Query("fee") poolFeePercent: String? = null,
                          @Query("cost") electricityCost: String? = null,
                          @Query("hcost") hardwareCost: String? = null): Observable<MiningCoinResponse>
}