package com.yadaniil.blogchain.screens.portfolio

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.realm.CoinEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/3/17.
 */

@InjectViewState
class PortfolioPresenter : MvpPresenter<PortfolioView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun getPortfolios() = repo.getAllPortfolio()
    fun getAllCcCoin() = repo.getAllCryptoCompareCoinsFromDb()

    fun downloadAndSaveAllCurrencies() {
        repo.getAllCoins(limit = "0")
                .subscribeOn(Schedulers.io())
                .map { CoinEntity.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showToolbarLoading()
                    viewState.hideSwipeRefreshLoading() }
                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ currenciesList ->
                    repo.saveCoinsToDb(currenciesList)
                }, { error ->
                    viewState.showLoadingError()
                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                })
    }

}