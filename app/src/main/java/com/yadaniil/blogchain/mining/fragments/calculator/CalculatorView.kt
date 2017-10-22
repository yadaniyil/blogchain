package com.yadaniil.blogchain.mining.fragments.calculator

import com.arellomobile.mvp.MvpView
import com.yadaniil.blogchain.data.api.models.MiningCoin
import com.yadaniil.blogchain.data.api.models.MiningCoinResponse

/**
 * Created by danielyakovlev on 9/29/17.
 */

interface CalculatorView : MvpView {

    fun initCalculatorView(coins: List<MiningCoin>)
    fun showLoading()
    fun showError()
    fun showTableLoading()
    fun showTableError()
    fun showTable(coin: MiningCoinResponse)

}