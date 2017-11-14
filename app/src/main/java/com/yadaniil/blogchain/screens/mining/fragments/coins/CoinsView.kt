package com.yadaniil.blogchain.screens.mining.fragments.coins

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.api.models.MiningCoin

/**
 * Created by danielyakovlev on 9/28/17.
 */


interface CoinsView : MvpView {
    fun showCoins(coins: List<MiningCoin>)
    fun showLoading()
    fun showError()
}