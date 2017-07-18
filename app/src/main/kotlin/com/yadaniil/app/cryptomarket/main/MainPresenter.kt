package com.yadaniil.app.cryptomarket.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.Repository
import com.yadaniil.app.cryptomarket.data.db.models.CryptoCompareCurrencyRealm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<IMainView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun getRealmCurrencies(): RealmResults<CryptoCompareCurrencyRealm>
            = repo.getAllCryptoCompareCurrenciesFromDb()

    fun downloadAndSaveAllCurrencies() {
        repo.getFullCurrenciesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { CryptoCompareCurrencyRealm.convertApiResponseToRealmList(it) }
                .doOnSubscribe { viewState.showLoading() }
                .subscribe({
                    currenciesList ->
                    repo.saveCryptoCompareCurrenciesToDb(currenciesList)
                    downloadAndSavePricesMultiFull()
                }, {
                    error ->
                    Timber.e(error.message)
                })
    }

    private fun downloadAndSavePricesMultiFull() {
        repo.getPriceMultiFull(buildFromSymbols(), buildToSymbols())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { viewState.stopLoading() }
                .subscribe({

                })
    }

    private fun buildFromSymbols(): String {
        val currencies = repo.getAllCryptoCompareCurrenciesFromDb().subList(0, 10)
        var symbols: String = ""
        for(currency in currencies) {
            symbols += currency.coinName + ","
        }
        return symbols
    }

    private fun buildToSymbols() = "BTC,USD"

    fun saveCryptoCompareCurrencyIcon(currencyRealm: CryptoCompareCurrencyRealm, byteArray: ByteArray)
            = repo.saveCryptoCompareCurrencyIcon(currencyRealm, byteArray)

}