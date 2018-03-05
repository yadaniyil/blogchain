package com.yadaniil.blogchain.screens.events

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by danielyakovlev on 1/8/18.
 */

class EventsViewModel(private val repo: Repository) : ViewModel() {

    fun getAllCoins() = repo.getAllCoinsFromDb()

    fun downloadAllEvents() {
        repo.downloadAllEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { viewState.showLoading() }
                .subscribe({ events ->
//                    viewState.stopLoading()
//                    viewState.showEvents(events.toMutableList())
                }, { error ->
//                    viewState.showLoadingError()
                    Timber.e(error.message)
                })
    }

}