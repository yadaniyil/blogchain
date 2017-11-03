package com.yadaniil.blogchain.screens.portfolio.addcoin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.utils.CurrencyHelper
import com.yadaniil.blogchain.utils.CurrencyHelper.getSymbolFromFullName
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/3/17.
 */

@InjectViewState
class AddToPortfolioPresenter : MvpPresenter<AddToPortfolioView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun showCoins() {
        repo.getAllCoinMarketCapCoinsFromDb().asObservable().subscribe({
            viewState.showCoin(it)
        }, { error ->
            Timber.e(error.message)
        })
    }

    fun getLinkForCoinImage(name: String): String {
        val symbol = getSymbolFromFullName(name)
        return CurrencyHelper.getImageLinkForCurrency(symbol, repo.getAllCryptoCompareCoinsFromDb())
    }

    fun addCoinToPortfolio() {

    }
}