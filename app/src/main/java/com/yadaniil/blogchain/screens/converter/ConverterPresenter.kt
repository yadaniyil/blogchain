package com.yadaniil.blogchain.screens.converter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.models.TickerResponse
import com.yadaniil.blogchain.utils.TickerParser
import io.reactivex.Observable
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

    fun showBothCryptoConversion(coinId: String, convertToSymbol: String) {
        repo.getCoin(coinId, convertToSymbol)
                .subscribeOn(Schedulers.io())
                .map { TickerParser.parseTickerResponse(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.startToolbarLoading() }
                .doOnComplete { viewState.stopToolbarLoading() }
                .subscribe({ ticker ->
                    viewState.proceedCryptToAnyConversion(ticker)
                }, { error ->

                    Timber.e(error.message)
                })
    }

    fun downloadTickerWithConversion(coinId: String, convertToSymbol: String): Observable<TickerResponse> {
        return repo.getCoin(coinId, convertToSymbol)
                .subscribeOn(Schedulers.io())
                .map { TickerParser.parseTickerResponse(it) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun cryptToAnyConversion(coinId: String, convertToSymbol: String) {
        repo.getCoin(coinId, convertToSymbol)
                .subscribeOn(Schedulers.io())
                .map { TickerParser.parseTickerResponse(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.startToolbarLoading()
                    viewState.disableAmountFields() }
                .doOnComplete {
                    viewState.stopToolbarLoading()
                    viewState.enableAmountFields() }
                .subscribe({ ticker ->
                    viewState.proceedCryptToAnyConversion(ticker)
                }, { error ->

                    Timber.e(error.message)
                })
    }

    fun fiatToCryptoConversion(coinId: String, convertToSymbol: String) {
        repo.getCoin(coinId, convertToSymbol)
                .subscribeOn(Schedulers.io())
                .map { TickerParser.parseTickerResponse(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.startToolbarLoading()
                    viewState.disableAmountFields() }
                .doOnComplete {
                    viewState.stopToolbarLoading()
                    viewState.enableAmountFields() }
                .subscribe({ ticker ->
                    viewState.proceedCryptToAnyConversion(ticker)
                }, { error ->

                    Timber.e(error.message)
                })
    }

}