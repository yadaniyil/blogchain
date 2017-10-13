package com.yadaniil.blogchain.data.api

import com.yadaniil.blogchain.data.api.models.MiningCoinsResponse
import com.yadaniil.blogchain.utils.Endpoints
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by danielyakovlev on 9/28/17.
 */


interface WhatToMineService {

    @GET(Endpoints.WHAT_TO_MINE_GPU_COINS_ENDPOINT)
    fun getAllGpuMiningCoins(): Observable<MiningCoinsResponse>

    @GET(Endpoints.WHAT_TO_MINE_ASIC_COINS_ENDPOINT)
    fun getAllAsicMiningCoins(): Observable<MiningCoinsResponse>
}