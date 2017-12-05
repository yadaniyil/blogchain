package com.yadaniil.blogchain.screens.home

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.BuildConfig
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/2/17.
 */

@InjectViewState
class HomePresenter : MvpPresenter<HomeView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun showChangelogDialog() {
        if(repo.getLastShowChangelogVersion() != BuildConfig.VERSION_CODE) {
            viewState.showChangelogDialog()
            repo.setLastShowChangelogVersion(BuildConfig.VERSION_CODE)
        }
    }

    fun downloadAndSaveAllCurrencies() {
        repo.getFullCurrenciesList()
                .subscribeOn(Schedulers.io())
                .map { CryptoCompareCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { downloadCMCList() }
                .subscribe({ currenciesList ->
                    repo.saveCryptoCompareCoinsToDb(currenciesList)
                }, { error ->
                    Timber.e(error.message)
                }
                )
    }

    private fun downloadCMCList() {
        repo.getAllCoins(limit = "0")
                .subscribeOn(Schedulers.io())
                .map { CoinMarketCapCurrencyRealm.convertApiResponseToRealmList(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ currenciesList ->
                    repo.saveCoinsToDb(currenciesList)
                }, { error ->
                    Timber.e(error.message)
                }
                )
    }

}