package com.yadaniil.blogchain.screens.converter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/15/17.
 */

@InjectViewState
class ConverterPresenter : MvpPresenter<ConverterView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun getAllCoins() = repo.getAllCoinsFromDb()
    fun getAllCcCoins() = repo.getAllCryptoCompareCoinsFromDb()


}