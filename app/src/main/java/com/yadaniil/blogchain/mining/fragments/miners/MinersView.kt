package com.yadaniil.blogchain.mining.fragments.miners

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.api.models.Miner

/**
 * Created by danielyakovlev on 9/20/17.
 */
interface MinersView : MvpView {

    fun showMiners(miners: List<Miner>)
}