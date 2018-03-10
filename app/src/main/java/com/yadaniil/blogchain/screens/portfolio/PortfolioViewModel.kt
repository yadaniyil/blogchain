package com.yadaniil.blogchain.screens.portfolio

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by danielyakovlev on 11/3/17.
 */

class PortfolioViewModel(private val repo: Repository) : ViewModel() {

    fun getPortfoliosLiveData() = repo.getAllPortfolioCoinsLiveData()

    fun downloadAndSaveAllCurrencies() {
        repo.getAllCoins(limit = "0")
                .subscribeOn(Schedulers.io())
//                .map { CoinEntity.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    viewState.showToolbarLoading()
//                    viewState.stopSwipeRefreshLoading() }
//                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ currenciesList ->
//                    repo.saveCoinsToDbAsync(currenciesList)
                }, { error ->
//                    viewState.showLoadingError()
//                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                })
    }

}