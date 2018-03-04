package com.yadaniil.blogchain.data.api.services

import com.yadaniil.blogchain.data.api.models.coindar.CoindarEventResponse
import com.yadaniil.blogchain.utils.Endpoints
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by danielyakovlev on 1/8/18.
 */

interface CoindarService {

    @GET(Endpoints.COINDAR_LAST_EVENTS_ENDPOINT)
    fun downloadAllEvents(): Observable<List<CoindarEventResponse>>

}