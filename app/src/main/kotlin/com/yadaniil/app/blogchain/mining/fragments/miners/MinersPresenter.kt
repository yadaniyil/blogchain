package com.yadaniil.app.blogchain.mining.fragments.miners

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.app.blogchain.Application
import com.yadaniil.app.blogchain.data.Repository
import com.yadaniil.app.blogchain.data.api.models.Miner
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
    var downloadedMiners: MutableList<Miner> = ArrayList()

    fun downloadMiners() {
        repo.getMiners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.idToMiner.values }
                .subscribe({ miners ->
                    downloadedMiners = miners.toMutableList()
                    viewState.showMiners(miners.toList())
                }, { error ->
                    Timber.e(error.message)
                })
    }

    fun findMinersByTags(filters: ArrayList<MinerFilterTag>): List<Miner> {
        val filteredMiners: MutableList<Miner> = ArrayList()
        for(miner in downloadedMiners) {
            val foundedMiner = filters.find {
                it.getText() == miner.equipmentType || it.getText() == miner.algorithm }
            if(foundedMiner != null)
                filteredMiners.add(miner)
        }
        return filteredMiners
    }
}