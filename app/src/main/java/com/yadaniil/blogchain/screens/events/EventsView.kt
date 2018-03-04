package com.yadaniil.blogchain.screens.events

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.api.models.coindar.CoindarEventResponse

/**
 * Created by danielyakovlev on 1/8/18.
 */

interface EventsView : MvpView {

    fun showLoading()
    fun stopLoading()
    fun showLoadingError()
    fun showEvents(events: MutableList<CoindarEventResponse>)


}