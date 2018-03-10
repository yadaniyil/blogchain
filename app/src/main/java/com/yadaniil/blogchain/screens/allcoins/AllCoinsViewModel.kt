package com.yadaniil.blogchain.screens.allcoins

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.api.models.coinmarketcap.TickerResponse
import com.yadaniil.blogchain.data.api.models.cryptocompare.CryptoCompareCurrenciesListResponse
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.utils.CoinMapper
import com.yadaniil.blogchain.utils.SnackbarMessage
import io.objectbox.android.ObjectBoxLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*


class AllCoinsViewModel(private val repo: Repository) : ViewModel() {

    val viewState: MutableLiveData<AllCoinsViewState> = MutableLiveData()
    val snackbarMessage: SnackbarMessage = SnackbarMessage()
    var coins: ObjectBoxLiveData<CoinEntity>

    init {
        viewState.value = ProgressBarLoadingViewState()
        coins = repo.getAllCoinsFromDbLiveData()
    }

    fun getCoinsLiveData(searchQuery: String? = null): ObjectBoxLiveData<CoinEntity> {
        if (searchQuery == null || searchQuery.isBlank())
            return repo.getAllCoinsFromDbLiveData()
        else
            return repo.getAllCoinsFilteredLiveData(searchQuery)
    }

    fun getAllCoinsSorted(fieldName: String, isDescending: Boolean) =
            repo.getAllCoinsSorted(fieldName, true)

    fun downloadAndSaveAllCurrencies(runSwipeToRefresh: Boolean? = null) {
        showLoading(runSwipeToRefresh)

        val allCoinsZipRequest = Observable.zip(
                repo.getFullCurrenciesList(),
                repo.getAllCoins(limit = "0"),
                BiFunction<CryptoCompareCurrenciesListResponse,
                        List<TickerResponse>, Pair<CryptoCompareCurrenciesListResponse,
                        List<TickerResponse>>> { ccAllCoins, cmcAllCoins ->
                    Pair(ccAllCoins, cmcAllCoins)
                })

        allCoinsZipRequest
                .subscribeOn(Schedulers.io())
                .flatMap {
                    Observable.fromCallable {
                        val coins = CoinMapper.mapCoins(it, repo.getAllCoinsFromDb())
                        AllCoinsHelper.coins = coins
                        repo.saveCoinsToDb(coins)
                        repo.saveLastCoinsUpdateTime(Date().time)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.value = StopSwipeRefreshLoadingViewState()
                    viewState.value = CoinsShowingViewState()
                }, { error ->
                    viewState.value = StopSwipeRefreshLoadingViewState()

                    if (AllCoinsHelper.coins.isEmpty())
                        viewState.value = LoadingErrorViewState()
                    else
                        snackbarMessage.postValue(R.string.update_error)
                    Timber.e(error.message)
                })
    }

    private fun showLoading(runSwipeToRefresh: Boolean?) {
        if (AllCoinsHelper.coins.isEmpty()) {
            viewState.value = ProgressBarLoadingViewState()
        } else {
            if (runSwipeToRefresh != null && runSwipeToRefresh == true)
                viewState.value = SwipeRefreshLoadingViewState()
        }
    }

    fun getLastCoinsUpdateTime() = repo.getLastCoinsUpdateTime()
}