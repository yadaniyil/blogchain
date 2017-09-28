package com.yadaniil.app.cryptomarket.mining.fragments.coins

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.data.Repository
import com.yadaniil.app.cryptomarket.data.api.models.MiningCoin
import com.yadaniil.app.cryptomarket.data.api.models.MiningCoinsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 9/28/17.
 */


@InjectViewState
class CoinsPresenter : MvpPresenter<CoinsView>() {

    init {
        Application.component?.inject(this)
    }

    @Inject lateinit var repo: Repository
    var downloadedCoins: List<MiningCoin> = ArrayList()

    fun downloadMiningCoins() {

        // TODO download GPU and ASIC coins simultaneously
        repo.getAllGpuMiningCoins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coins ->
                    downloadedCoins = addNamesToCoins(coins)
                    viewState.showCoins(downloadedCoins)
                }, { error ->
                    Timber.e(error.message)
                })
    }

    private fun addNamesToCoins(response: MiningCoinsResponse): List<MiningCoin> {
        val names = response.gpuCoins.keys.toList()
        val coins = response.gpuCoins.values.toList()

        names.forEachIndexed { index, s -> coins[index].name = s }

        return coins
    }
}