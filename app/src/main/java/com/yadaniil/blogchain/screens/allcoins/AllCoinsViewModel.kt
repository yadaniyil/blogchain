package com.yadaniil.blogchain.screens.allcoins

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.models.coinmarketcap.TickerResponse
import com.yadaniil.blogchain.data.api.models.cryptocompare.CryptoCompareCurrenciesListResponse
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.utils.CoinMapper
import io.objectbox.android.ObjectBoxLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*


class AllCoinsViewModel(private val repo: Repository) : ViewModel() {

    fun getAllCoinsFromDb(): List<CoinEntity> = repo.getAllCoinsFromDb()
    fun getAllCoinsFromDbLiveData(): ObjectBoxLiveData<CoinEntity> = repo.getAllCoinsFromDbLiveData()
    fun getAllCoinsFiltered(text: String): List<CoinEntity> = repo.getAllCoinsFiltered(text)
    fun getAllCoinsFilteredLiveData(text: String) = repo.getAllCoinsFilteredLiveData(text)
    fun getAllCoinsSorted(fieldName: String, isDescending: Boolean) =
            repo.getAllCoinsSorted(fieldName, true)

    class FullUpdateResponse(val ccAllCoinsResponse: CryptoCompareCurrenciesListResponse,
                             val cmcAllCoinsResponse: List<TickerResponse>)

    fun downloadAndSaveAllCurrencies() {
        val allCoinsZipRequest = Observable.zip(
                repo.getFullCurrenciesList(),
                repo.getAllCoins(limit = "0"),
                BiFunction<CryptoCompareCurrenciesListResponse,
                        List<TickerResponse>, FullUpdateResponse> { ccAllCoins, cmcAllCoins ->
                    FullUpdateResponse(ccAllCoins, cmcAllCoins)
                })

        allCoinsZipRequest
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ fullUpdateResponse ->
                    repo.saveCoinsToDb(CoinMapper.mapCoins(fullUpdateResponse, getAllCoinsFromDb()))
                    repo.saveLastCoinsUpdateTime(Date().time)
//                    updateLastCoinsUpdateTime()
                }, { error ->
                    Timber.e(error.message)
                })
    }

//    fun updateLastCoinsUpdateTime() =
//            viewState.showLastCoinsUpdateTime(repo.getLastCoinsUpdateTime())


    fun addCurrencyToFavourite(currency: CoinEntity) {
        repo.addCoinToFavourite(currency)
//        viewState.onCurrencyAddedToFavourite(currency)
    }
}