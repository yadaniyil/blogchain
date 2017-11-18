package com.yadaniil.blogchain.screens.converter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.utils.TickerParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
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
    fun getCoin(symbol: String) = repo.getCoinFromDb(symbol)

    fun downloadCoin(coinId: String, convertToCurrency: String) {
        repo.getCoin(coinId, convertToCurrency)
                .subscribeOn(Schedulers.io())
                .map { TickerParser.parseTickerResponse(it) }
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ responseString ->
//                    CoinMarketCapCurrencyRealm.convertApiResponseToRealmList(it)
                }, { error ->
//                    viewState.showLoadingError()
//                    viewState.stopToolbarLoading()
                    Timber.e(error.message)
                }
                )
    }

}