package com.yadaniil.app.cryptomarket.mining.fragments.calculator

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.Repository
import javax.inject.Inject

/**
 * Created by danielyakovlev on 9/29/17.
 */


@InjectViewState
class CalculatorPresenter : MvpPresenter<CalculatorView>() {

    init {
        Application.component?.inject(this)
    }

    @Inject lateinit var repo: Repository

}