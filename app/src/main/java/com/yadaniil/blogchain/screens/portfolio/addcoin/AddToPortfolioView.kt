package com.yadaniil.blogchain.screens.portfolio.addcoin

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.db.models.realm.CoinEntity

/**
 * Created by danielyakovlev on 11/3/17.
 */

interface AddToPortfolioView : MvpView {
    fun showCoin(coins: List<CoinEntity>)
}