package com.yadaniil.blogchain.screens.portfolio.addcoin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.yadaniil.blogchain.Application
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.utils.CurrencyHelper
import com.yadaniil.blogchain.utils.CurrencyHelper.getSymbolFromFullName
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by danielyakovlev on 11/3/17.
 */

@InjectViewState
class AddToPortfolioPresenter : MvpPresenter<AddToPortfolioView>() {

    @Inject lateinit var repo: Repository

    init {
        Application.component?.inject(this)
    }

    fun showCoins() {
        repo.getAllCoinsFromDb().asObservable().subscribe({
            viewState.showCoin(it)
        }, { error ->
            Timber.e(error.message)
        })
    }

    fun getLinkForCoinImage(name: String): String {
        val symbol = getSymbolFromFullName(name)
        val coin = repo.getCoinFromDb(symbol)
        return CurrencyHelper.getImageLinkForCurrency(coin, repo.getAllCryptoCompareCoinsFromDb())
    }

    fun addCoinToPortfolio(coinSymbol: String, amountOfCoins: String, buyPriceOfCoin: String,
                           storageType: String, storageName: String, portfolioToEdit: PortfolioRealm?) {
        val coin = repo.getCoinFromDb(coinSymbol)
        if(portfolioToEdit != null)
            repo.editPortfolio(portfolioToEdit, coin, amountOfCoins, buyPriceOfCoin, storageType, storageName)
        else
            repo.addCoinToPortfolio(coin, amountOfCoins, buyPriceOfCoin, storageType, storageName)
    }

//    fun editPortfolio(portfolio: PortfolioRealm, coin: CoinMarketCapCurrencyRealm,
//                      amountOfCoins: String, buyPriceOfCoin: String, storageType: String,
//                      storageName: String)
//            = repo.editPortfolio(portfolio, coin, amountOfCoins, buyPriceOfCoin, storageType, storageName)

    fun getSinglePortfolio(portfolioId: String) = repo.getSinglePortfolio(portfolioId)
    fun removeItemFromPortfolio(id: String?) = repo.removeItemFromPortfolio(id ?: "")
}