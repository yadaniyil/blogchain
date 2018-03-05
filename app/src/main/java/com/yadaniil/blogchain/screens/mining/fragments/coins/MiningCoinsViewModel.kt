package com.yadaniil.blogchain.screens.mining.fragments.coins

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoin
import com.yadaniil.blogchain.data.api.models.whattomine.MiningCoinsResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by danielyakovlev on 9/28/17.
 */


class MiningCoinsViewModel(private val repo: Repository) : ViewModel() {

    private var downloadedCoins: MutableList<MiningCoin> = ArrayList()

    fun downloadMiningCoins() {
        val allCoinsZipRequest =  Observable.zip(repo.getAllGpuMiningCoins(), repo.getAllAsicMiningCoins(),
                BiFunction<MiningCoinsResponse, MiningCoinsResponse, List<MiningCoin>> { gpus, asics ->
            val gpuCoins = addNameAndEquipmentTypeToCoins(gpus, "GPU")
            val asicCoins = addNameAndEquipmentTypeToCoins(asics, "ASIC")
            downloadedCoins.addAll(gpuCoins)
            downloadedCoins.addAll(asicCoins)
            downloadedCoins
        })

        allCoinsZipRequest
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { viewState.showLoading() }
                .subscribe({ allCoins ->
                    Timber.e("All coins size: " + allCoins.size)
//                    viewState.showCoins(allCoins)
                }, { error ->
//                    viewState.showError()
                    Timber.e(error.message)
                })
    }

    private fun addNameAndEquipmentTypeToCoins(response: MiningCoinsResponse,
                                               equipmentType: String): List<MiningCoin> {
        val names = response.miningCoins.keys.toList()
        val coins = response.miningCoins.values.toList()
        Timber.e(equipmentType + " coins size: " + coins.size)

        names.forEachIndexed { index, s -> coins[index].name = s; coins[index].equipmentType = equipmentType }

        return coins
    }

    fun getFilteredCoins(query: String): List<MiningCoin> {
        return downloadedCoins.filter { it.tag.startsWith(query, true) ||
                it.name.startsWith(query, true) }
    }

    fun getAllCmcCurrencies() = repo.getAllCoinsFromDb()
}