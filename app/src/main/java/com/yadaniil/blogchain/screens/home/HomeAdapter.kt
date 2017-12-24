package com.yadaniil.blogchain.screens.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.screens.portfolio.PortfolioHelper
import com.yadaniil.blogchain.utils.AmountFormatter
import com.yadaniil.blogchain.utils.ImageLoader
import com.yadaniil.blogchain.utils.ListHelper
import io.reactivex.rxkotlin.toObservable
import io.realm.RealmResults
import timber.log.Timber

/**
 * Created by danielyakovlev on 12/5/17.
 */


class HomeAdapter(private val sections: MutableList<HomeListSection>,
                  private val context: Context,
                  private val presenter: HomePresenter)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_PORTFOLIO = 0
    private val TYPE_COINS = 1

    private lateinit var coinsHolder: ListHelper.HomeCoinsHolder
    private lateinit var portfolioHolder: ListHelper.HomePortfolioHolder


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        return when (viewType) {
            TYPE_PORTFOLIO -> {
                itemView = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_home_portfolio_balance, parent, false)
                portfolioHolder = ListHelper.HomePortfolioHolder(itemView)
                portfolioHolder
            }
            TYPE_COINS -> {
                itemView = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_home_coins, parent, false)
                coinsHolder = ListHelper.HomeCoinsHolder(itemView)
                coinsHolder
            }
            else -> {
                itemView = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_news, parent, false)
                ListHelper.NewsHolder(itemView)
            }
        }
    }

    override fun getItemViewType(position: Int) =
            when (sections[position]) {
                is PortfolioSection -> TYPE_PORTFOLIO
                is CoinsSection -> TYPE_COINS
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val item = sections[position]
        when (holder) {
            is ListHelper.HomePortfolioHolder -> bindPortfolio(holder, item as PortfolioSection)
            is ListHelper.HomeCoinsHolder -> bindCoins(holder, item as CoinsSection)
        }
    }

    override fun getItemCount() = sections.size

    private fun bindPortfolio(holder: ListHelper.HomePortfolioHolder, item: PortfolioSection) {
        PortfolioHelper.updateTotalFiatBalance(item.portfolios, holder.totalAmount, holder.totalAmountBtc)
    }

    fun updateCoins(coins: RealmResults<CoinMarketCapCurrencyRealm>) {
        try {
            bindCoins(coinsHolder, CoinsSection(coins))
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    fun updatePortfolio(portfolios: RealmResults<PortfolioRealm>) {
        try {
            bindPortfolio(portfolioHolder, PortfolioSection(portfolios))
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    private fun bindCoins(holder: ListHelper.HomeCoinsHolder, coinsItem: CoinsSection) {
        val coins: MutableList<CoinMarketCapCurrencyRealm> = ArrayList()
        presenter.getAllCoins().toObservable().subscribe({
            coins.add(it)
            if(it.rank > 4) return@subscribe
        }, {
            Timber.e(it.message)
        }, {
            if (coins.isNotEmpty()) {
                ImageLoader.loadCoinIcon(coins[0].symbol ?: "", holder.firstCoinIcon,
                        context, presenter.repo)
                ImageLoader.loadCoinIcon(coins[1].symbol ?: "", holder.secondCoinIcon,
                        context, presenter.repo)
                ImageLoader.loadCoinIcon(coins[2].symbol ?: "", holder.thirdCoinIcon,
                        context, presenter.repo)
                ImageLoader.loadCoinIcon(coins[3].symbol ?: "", holder.forthCoinIcon,
                        context, presenter.repo)

                holder.firstCoinName.text = coins[0].name
                holder.firstCoinPrice.text = "$${AmountFormatter.formatFiatPrice(coinsItem.coin[0].priceUsd.toString())}"

                holder.secondCoinName.text = coins[1].name
                holder.secondCoinPrice.text = "$${AmountFormatter.formatFiatPrice(coinsItem.coin[1].priceUsd.toString())}"

                holder.thirdCoinName.text = coins[2].name
                holder.thirdCoinPrice.text = "$${AmountFormatter.formatFiatPrice(coinsItem.coin[2].priceUsd.toString())}"

                holder.forthCoinName.text = coins[3].name
                holder.forthCoinPrice.text = "$${AmountFormatter.formatFiatPrice(coinsItem.coin[3].priceUsd.toString())}"
            }
        })
    }

}