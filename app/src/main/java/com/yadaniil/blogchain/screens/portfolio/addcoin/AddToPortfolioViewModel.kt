package com.yadaniil.blogchain.screens.portfolio.addcoin

import android.arch.lifecycle.ViewModel
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity

/**
 * Created by danielyakovlev on 11/3/17.
 */

class AddToPortfolioViewModel(private val repo: Repository) : ViewModel() {

    fun showCoins() {
//        repo.getAllCoinsFromDb().asFlowable().subscribe({
//            viewState.showCoin(it)
//        }, { error ->
//            Timber.e(error.message)
//        })
    }

    fun getCoin(symbol: String): CoinEntity? {
        return repo.getCoinFromDb(symbol)
    }

    fun addCoinToPortfolio(coinSymbol: String, amountOfCoins: String, buyPriceOfCoin: String,
                           storageType: String, storageName: String, portfolioToEdit: PortfolioCoinEntity?,
                           description: String) {
        val coin = repo.getCoinFromDb(coinSymbol)
//        if(portfolioToEdit != null)
//            repo.savePortfolioCoin(portfolioToEdit, coin!!, amountOfCoins, buyPriceOfCoin, storageType, storageName, description)
//        else
//            repo.addCoinToPortfolio(coin!!, amountOfCoins, buyPriceOfCoin, storageType, storageName, description)
    }

    fun getSinglePortfolio(portfolioEntityId: Long) = repo.getSinglePortfolioCoin(portfolioEntityId)
    fun removeItemFromPortfolio(portfolioEntityId: Long?) = portfolioEntityId?.let {
        repo.removePortfolioCoin(it)
    }
}