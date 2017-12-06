package com.yadaniil.blogchain.screens.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.utils.AmountFormatter
import com.yadaniil.blogchain.utils.ImageLoader
import com.yadaniil.blogchain.utils.ListHelper
import java.math.BigDecimal

/**
 * Created by danielyakovlev on 12/5/17.
 */


class HomeAdapter(private val items: MutableList<HomeListItem>, private val context: Context,
                  private val presenter: HomePresenter)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_PORTFOLIO = 0
    private val TYPE_COINS = 1
    private val TYPE_NEWS = 2


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        return when (viewType) {
            TYPE_PORTFOLIO -> {
                itemView = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_home_portfolio_balance, parent, false)
                ListHelper.HomePortfolioHolder(itemView)
            }
            TYPE_COINS -> {
                itemView = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_home_coins, parent, false)
                ListHelper.HomeCoinsHolder(itemView)
            }
            else -> {
                itemView = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_home_news, parent, false)
                ListHelper.HomeNewsHolder(itemView)
            }
        }
    }

    override fun getItemViewType(position: Int) =
            when (items[position]) {
                is PortfolioItem -> TYPE_PORTFOLIO
                is CoinsItem -> TYPE_COINS
                is NewsItem -> TYPE_NEWS
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val item = items[position]
        when (holder) {
            is ListHelper.HomePortfolioHolder -> bindPortfolio(holder, item as PortfolioItem)
            is ListHelper.HomeCoinsHolder -> bindCoins(holder, item as CoinsItem)
            is ListHelper.HomeNewsHolder -> holder.header.text = (item as NewsItem).header
        }
    }

    override fun getItemCount() = items.size

    private fun bindPortfolio(holder: ListHelper.HomePortfolioHolder, item: PortfolioItem) {
        holder.totalAmount.text = item.balanceUsd
    }

    private fun bindCoins(holder: ListHelper.HomeCoinsHolder, coinsItem: CoinsItem) {
        if(coinsItem.coin.isNotEmpty()) {
            ImageLoader.loadCoinIcon(coinsItem.coin[0].symbol ?: "", holder.firstCoinIcon,
                    context, presenter.repo)
            ImageLoader.loadCoinIcon(coinsItem.coin[1].symbol ?: "", holder.secondCoinIcon,
                    context, presenter.repo)
            ImageLoader.loadCoinIcon(coinsItem.coin[2].symbol ?: "", holder.thirdCoinIcon,
                    context, presenter.repo)
            ImageLoader.loadCoinIcon(coinsItem.coin[3].symbol ?: "", holder.forthCoinIcon,
                    context, presenter.repo)

            holder.firstCoinName.text = coinsItem.coin[0].name
            holder.firstCoinPrice.text = "$${AmountFormatter.formatFiatPrice(coinsItem.coin[0].priceUsd.toString())}"

            holder.secondCoinName.text = coinsItem.coin[1].name
            holder.secondCoinPrice.text = "$${AmountFormatter.formatFiatPrice(coinsItem.coin[1].priceUsd.toString())}"

            holder.thirdCoinName.text = coinsItem.coin[2].name
            holder.thirdCoinPrice.text = "$${AmountFormatter.formatFiatPrice(coinsItem.coin[2].priceUsd.toString())}"

            holder.forthCoinName.text = coinsItem.coin[3].name
            holder.forthCoinPrice.text = "$${AmountFormatter.formatFiatPrice(coinsItem.coin[3].priceUsd.toString())}"
        }
    }

}