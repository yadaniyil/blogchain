package com.yadaniil.blogchain.screens.mining.fragments.miners

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.api.models.cryptocompare.Miner

/**
 * Created by danielyakovlev on 9/20/17.
 */
interface MinersView : MvpView {

    fun showMiners(miners: List<Miner>)
    fun showError()
    fun showLoading()
}