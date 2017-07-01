package com.yadaniil.app.cryptomarket.main

import com.yadaniil.app.cryptomarket.data.Repository
import com.yadaniil.app.cryptomarket.data.db.models.CurrencyRealm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import javax.inject.Inject


class MainPresenter @Inject constructor(private val view: IMainView) {

    @Inject lateinit var repo: Repository

    fun getRealmCurrencies(): RealmResults<CurrencyRealm> = repo.getAllCurrenciesFromDb()

    fun downloadAndSaveAllCurrencies() {
        repo.getAllCurrencies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.stopLoading() }
                .subscribe {
                    currenciesList ->
                    repo.saveCurrenciesToDb(
                            CurrencyRealm.convertApiResponseToRealmList(currenciesList))
                }
    }

}