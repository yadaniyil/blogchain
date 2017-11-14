package com.yadaniil.blogchain.screens.mining.fragments.calculator

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.models.MiningCoin
import com.yadaniil.blogchain.data.api.models.MiningCoinsResponse
import com.yadaniil.blogchain.utils.CurrencyHelper
import com.yadaniil.blogchain.utils.CurrencyHelper.getSymbolFromFullName
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 9/29/17.
 */


@InjectViewState
class CalculatorPresenter : MvpPresenter<CalculatorView>() {

    init {
        Application.component?.inject(this)
    }

    @Inject lateinit var repo: Repository

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
                .doOnSubscribe { viewState.showLoading() }
                .subscribe({ allCoins ->
                    Timber.e("All coins size: " + allCoins.size)
                    viewState.initCalculatorView(allCoins)
                }, { error ->
                    viewState.showError()
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

    fun getLinkForCoinImage(fullName: String): String {
        if(fullName.startsWith("Nicehash", true))
            return ""

        val symbol = getSymbolFromFullName(fullName)
        val cryptoCoin = repo.getCoinFromDb(symbol)
        return CurrencyHelper.getImageLinkForCurrency(cryptoCoin, repo.getAllCryptoCompareCoinsFromDb())
    }

    fun getHashrateExponentForCoin(fullCoinName: String): String {
        val symbol = getSymbolFromFullName(fullCoinName)
        val nethash = downloadedCoins.find { it.tag == symbol }?.nethash ?: ""
        return calculateHashrateExponent(nethash)
    }

    fun calculateHashrateExponent(nethash: String) = when {
        nethash.length > 15 -> "Gh/s"
        nethash.length in 10..15 -> "Mh/s"
        else -> "h/s"
    }

    fun calculateTable(coinFullName: String, hashrate: String, power: String,
                       cost: String, poolFeePercent: String) {
        val symbol = getSymbolFromFullName(coinFullName)
        val coinId = downloadedCoins.find { it.tag == symbol }?.id ?: ""

        repo.getCoinById(coinId.toString(), hashrate, power, poolFeePercent, cost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showTableLoading() }
                .subscribe({ coin ->
                    viewState.showTable(coin)
                }, { error ->
                    viewState.showTableError()
                    Timber.e(error.message)
                })
    }

}