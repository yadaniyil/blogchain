package com.yadaniil.blogchain.screens.events

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 1/8/18.
 */

@InjectViewState
class EventsPresenter : MvpPresenter<EventsView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun downloadAllEvents() {
        repo.downloadAllEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showLoading() }
                .subscribe({ events ->
                    viewState.stopLoading()
                    viewState.showEvents(events.toMutableList())
                }, { error ->
                    viewState.showLoadingError()
                    Timber.e(error.message)
                })
    }

}