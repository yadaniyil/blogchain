package com.yadaniil.blogchain.screens.findcurrency

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/2/17.
 */

@InjectViewState
class FindCurrencyPresenter : MvpPresenter<FindCurrencyView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }
}