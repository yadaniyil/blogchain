package com.yadaniil.blogchain.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.screens.portfolio.PortfolioAdapter
import org.jetbrains.anko.*
import java.math.BigDecimal

/**
 * Created by danielyakovlev on 10/31/17.
 */

object ListHelper {

    interface OnCoinClickListener {
        fun onClick(holder: ListHelper.FindCoinHolder?, currencyRealm: CoinEntity)
    }

    // region Coin
    class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemRootLayout: LinearLayout = view.find(R.id.item_root_layout)
        var symbol: TextView = view.find(R.id.item_currency_symbol)
        var name: TextView = view.find(R.id.item_currency_name)
        var usdRate: TextView = view.find(R.id.item_currency_price)
        var btcRate: TextView = view.find(R.id.item_currency_btc_price)
        var hourChange: TextView = view.find(R.id.item_currency_hour_change)
        var dayChange: TextView = view.find(R.id.item_currency_day_change)
        var weekChange: TextView = view.find(R.id.item_currency_week_change)
        var icon: ImageView = view.find(R.id.item_currency_icon)
        var rank: TextView = view.find(R.id.item_currency_rank)
        var data: CoinEntity? = null
    }

    fun bindCoinListItem(coinHolder: CoinViewHolder, coinEntity: CoinEntity, context: Context,
                         onClick: CoinClickListener, onLongClick: CoinLongClickListener, removeRank: Boolean) {
        with(coinHolder) {
            if (removeRank) rank.visibility = View.GONE else rank.text = coinEntity.rank.toString()

            data = coinEntity
            symbol.text = coinEntity.symbol
            name.text = coinEntity.name
            usdRate.text = "${AmountFormatter.formatFiatPrice(BigDecimal(coinEntity?.priceUsd).toString())} USD"
            btcRate.text = "${AmountFormatter.formatCryptoPrice(BigDecimal(coinEntity.priceBtc).toString())} BTC"
            itemRootLayout.onClick { onClick.onClick(coinHolder, coinEntity) }
            itemRootLayout.onLongClick { onLongClick.onLongClick(coinHolder, coinEntity); true }
            initRatesChange(this, coinEntity, context)

            if (coinEntity.iconBytes == null) {
                ImageLoader.loadCoinIcon(coinEntity, icon, context)
            } else {
//                val bitmapIcon = BitmapFactory
//                        .decodeStream(java.io.ByteArrayInputStream(coinEntity.iconBytes))
//                icon.setImageBitmap(bitmapIcon)
            }
        }
    }

    private fun initRatesChange(coinViewHolder: CoinViewHolder,
                                currencyRealm: CoinEntity?, context: Context) {
        with(coinViewHolder) {
            hourChange.text = "${currencyRealm?.percentChange1h} %"
            dayChange.text = "${currencyRealm?.percentChange24h} %"
            weekChange.text = "${currencyRealm?.percentChange7d} %"

            if (hourChange.text.startsWith("-"))
                hourChange.setTextColor(context.resources.getColor(R.color.md_red_900))
            else
                hourChange.setTextColor(context.resources.getColor(R.color.md_green_900))

            if (dayChange.text.startsWith("-"))
                dayChange.setTextColor(context.resources.getColor(R.color.md_red_900))
            else
                dayChange.setTextColor(context.resources.getColor(R.color.md_green_900))

            if (weekChange.text.startsWith("-"))
                weekChange.setTextColor(context.resources.getColor(R.color.md_red_900))
            else
                weekChange.setTextColor(context.resources.getColor(R.color.md_green_900))

            arrayOf(hourChange, dayChange, weekChange)
                    .filterNot { it.text.startsWith("-") }
                    .forEach { it.text = "+" + it.text }

            if (currencyRealm?.percentChange1h == null) {
                hourChange.text = "?"
                hourChange.setTextColor(context.resources.getColor(R.color.md_black_1000))
            }
            if (currencyRealm?.percentChange24h == null) {
                dayChange.text = "?"
                dayChange.setTextColor(context.resources.getColor(R.color.md_black_1000))
            }
            if (currencyRealm?.percentChange7d == null) {
                weekChange.text = "?"
                weekChange.setTextColor(context.resources.getColor(R.color.md_black_1000))
            }
        }
    }
    // endregion Coin

    // region FindCoin
    class FindCoinHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemRootLayout: LinearLayout = view.find(R.id.simple_item_root)
        var coinName: TextView = view.find(R.id.coin_name)
        var coinSymbol: TextView = view.find(R.id.coin_symbol)
        var coinIcon: ImageView = view.find(R.id.coin_icon)
    }

    fun bindFindCoin(holder: FindCoinHolder, coinEntity: CoinEntity?,
                     onClick: OnCoinClickListener, context: Context) {
        holder.coinName.text = coinEntity?.name
        holder.coinSymbol.text = coinEntity?.symbol
        holder.itemRootLayout.onClick { onClick.onClick(holder, coinEntity!!) }
        ImageLoader.loadCoinIcon(coinEntity, holder.coinIcon, context)
    }
    // endregion FindCoin

    // region FindFiat
    class FiatHeaderHolder(view: View) : RecyclerView.ViewHolder(view) {
        var text: TextView = view.find(R.id.fiat_header_text)
    }

    class FindFiatHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemRootLayout: LinearLayout = view.find(R.id.simple_item_root)
        var fiatName: TextView = view.find(R.id.fiat_name)
        var fiatSymbol: TextView = view.find(R.id.fiat_symbol)
        var fiatFlagIcon: ImageView = view.find(R.id.fiat_flag)
    }
    // endregion FindFiat

    // region Portfolio
    class PortfolioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemRootLayout: LinearLayout = view.find(R.id.item_root_layout)
        var symbol: TextView = view.find(R.id.item_currency_symbol)
        var name: TextView = view.find(R.id.item_currency_name)
        var amountOfCoins: TextView = view.find(R.id.item_coin_amount)
        var fiatBalance: TextView = view.find(R.id.item_fiat_balance)
        var profitPercentage: TextView = view.find(R.id.item_profit_percentage)
        var icon: ImageView = view.find(R.id.item_currency_icon)
    }

    fun bindPortfolioItem(holder: PortfolioViewHolder, portfolioCoinEntity: PortfolioCoinEntity,
                          context: Context, onClick: PortfolioAdapter.OnClick,
                          onLongClick: PortfolioAdapter.OnLongClick) {
        with(holder) {
            symbol.text = portfolioCoinEntity.coin?.target?.symbol
            name.text = portfolioCoinEntity.coin?.target?.name
            val amountOfCoinsString = "${portfolioCoinEntity.amountOfCoins} " +
                    "${portfolioCoinEntity.coin?.target?.symbol}"
            amountOfCoins.text = amountOfCoinsString
            val fiatBalanceString = "${AmountFormatter
                    .formatFiatPrice(calculatePortfolioFiatSum(portfolioCoinEntity))} " +
                    context.getString(R.string.usd)
            fiatBalance.text = fiatBalanceString
            setPortfolioProfit(portfolioCoinEntity, profitPercentage, context)
            itemRootLayout.onClick { onClick.onClick(holder, portfolioCoinEntity) }
            itemRootLayout.onLongClick { onLongClick.onLongClick(holder, portfolioCoinEntity); true }

            ImageLoader.loadCoinIcon(portfolioCoinEntity.coin?.target, holder.icon, context)
        }
    }

    private fun setPortfolioProfit(portfolioCoinEntity: PortfolioCoinEntity, profitPercentage: TextView, context: Context) {
        // Setting value
        if (portfolioCoinEntity.buyPriceInFiat.isBlank())
            profitPercentage.text = "?"
        else
            profitPercentage.text = calculateProfit(portfolioCoinEntity.coin?.target?.priceUsd,
                    portfolioCoinEntity.buyPriceInFiat.toDoubleOrNull())

        // Setting color
        if (profitPercentage.text.startsWith("-"))
            profitPercentage.setTextColor(context.resources.getColor(R.color.md_red_900))
        else if (profitPercentage.text != "?")
            profitPercentage.setTextColor(context.resources.getColor(R.color.md_green_900))

        if (profitPercentage.text != "?")
            profitPercentage.text = "${profitPercentage.text} %"
    }

    private fun calculateProfit(currentPrice: Double?, buyPrice: Double?): String {
        if (currentPrice == null || buyPrice == null) return "?"

        val increase = currentPrice - buyPrice
        val result = increase / buyPrice * 100
        return AmountFormatter.formatFiatPrice(result.toString())
    }

    fun calculatePortfolioFiatSum(portfolioCoinEntity: PortfolioCoinEntity) =
            (BigDecimal(portfolioCoinEntity.amountOfCoins) *
                    BigDecimal(portfolioCoinEntity.coin?.target?.priceUsd ?: 0.0))

    fun calculatePortfolioBtcSum(portfolioCoinEntity: PortfolioCoinEntity) =
            (BigDecimal(portfolioCoinEntity.amountOfCoins) *
                    BigDecimal(portfolioCoinEntity.coin?.target?.priceBtc ?: 0.0))
    // endregion Portfolio

    // region News
    class NewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.find(R.id.news_header)
        var pubDate: TextView = view.find(R.id.news_publish_date)
        var sourceName: TextView = view.find(R.id.news_source_name)
        var image: ImageView = view.find(R.id.news_image)
        var rootView: LinearLayout = view.find(R.id.item_root_view)
    }
    // endregion News
}