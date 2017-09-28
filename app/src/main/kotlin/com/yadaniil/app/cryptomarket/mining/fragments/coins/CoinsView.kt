package com.yadaniil.app.cryptomarket.mining.fragments.coins

import com.arellomobile.mvp.MvpView
import com.yadaniil.app.cryptomarket.data.api.models.MiningCoin

/**
 * Created by danielyakovlev on 9/28/17.
 */


interface CoinsView : MvpView {
    fun showCoins(coins: List<MiningCoin>)
}