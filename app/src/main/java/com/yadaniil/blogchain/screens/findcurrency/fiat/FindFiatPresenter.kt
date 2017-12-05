package com.yadaniil.blogchain.screens.findcurrency.fiat

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/15/17.
 */

@InjectViewState
class FindFiatPresenter : MvpPresenter<FindFiatView>() {

    init {
        Application.component?.inject(this)
    }

    @Inject lateinit var repo: Repository

}