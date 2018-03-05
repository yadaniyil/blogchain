package com.yadaniil.blogchain.screens.mining.fragments.miners

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.models.cryptocompare.Miner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by danielyakovlev on 9/20/17.
 */

class MinersViewModel(private val repo: Repository) : ViewModel() {

    var downloadedMiners: MutableList<Miner> = ArrayList()

    fun downloadMiners() {
        repo.getMiners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { viewState.showLoading() }
                .map { it.idToMiner.values }
                .subscribe({ miners ->
                    downloadedMiners = miners.toMutableList()
//                    viewState.showMiners(miners.toList())
                }, { error ->
//                    viewState.showError()
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