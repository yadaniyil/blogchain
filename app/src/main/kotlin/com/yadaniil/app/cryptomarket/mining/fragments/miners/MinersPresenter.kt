package com.yadaniil.app.cryptomarket.mining.fragments.miners

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 9/20/17.
 */

@InjectViewState
class MinersPresenter : MvpPresenter<MinersView>() {

    init {
        Application.component?.inject(this)
    }

    @Inject lateinit var repo: Repository

    fun downloadMiners() {
        repo.getMiners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.idToMiner.values }
//                .doOnSubscribe { viewState.showLoading() }
//                .doOnComplete { viewState.stopLoading() }
                .subscribe({ miners ->
                    viewState.showMiners(miners.toMutableList())
                }, { error ->
                    Timber.e(error.message)
                })
    }

}