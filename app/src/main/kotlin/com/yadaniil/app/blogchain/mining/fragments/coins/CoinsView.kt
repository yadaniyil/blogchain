package com.yadaniil.app.blogchain.mining.fragments.coins

import com.arellomobile.mvp.MvpView
import com.yadaniil.app.blogchain.data.api.models.MiningCoin

/**
 * Created by danielyakovlev on 9/28/17.
 */


interface CoinsView : MvpView {
    fun showCoins(coins: List<MiningCoin>)
}