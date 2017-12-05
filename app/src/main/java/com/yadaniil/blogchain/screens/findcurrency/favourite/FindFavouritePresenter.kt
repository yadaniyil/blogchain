package com.yadaniil.blogchain.screens.findcurrency.favourite

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/15/17.
 */

@InjectViewState
class FindFavouritePresenter : MvpPresenter<FindFavouriteView>() {

    init {
        Application.component?.inject(this)
    }

    @Inject lateinit var repo: Repository

    fun getFavouriteCoins() = repo.getAllFavouriteCoins()

    fun getFavouriteCoinsFiltered(text: String) = repo.getFavouriteCoinsFiltered(text)

    fun getCcCoins() = repo.getAllCryptoCompareCoinsFromDb()
}