package com.yadaniil.blogchain.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.screens.findcoin.FindCoinAdapter
import com.yadaniil.blogchain.screens.portfolio.PortfolioAdapter
import org.jetbrains.anko.*
import timber.log.Timber
import java.math.BigDecimal

/**
 * Created by danielyakovlev on 10/31/17.
 */

object ListHelper {

    // region Currency
    fun bindCurrency(coinHolder: CoinViewHolder, currencyRealm: CoinMarketCapCurrencyRealm,
                     ccList: MutableList<CryptoCompareCurrencyRealm>, context: Context,
                     onClick: CoinClickListener, onLongClick: CoinLongClickListener, removeRank: Boolean) {
        with(coinHolder) {
            if (removeRank) rank.visibility = View.GONE else rank.text = currencyRealm.rank.toString()

            data = currencyRealm
            symbol.text = currencyRealm.symbol
            name.text = currencyRealm.name
            usdRate.text = AmountFormatter.formatFiatPrice(BigDecimal(currencyRealm?.priceUsd).toString()) + " USD"
            btcRate.text = AmountFormatter.formatCryptoPrice(BigDecimal(currencyRealm.priceBtc).toString()) + " BTC"
            itemRootLayout.onClick { onClick.onClick(coinHolder, currencyRealm) }
            itemRootLayout.onLongClick { onLongClick.onLongClick(coinHolder, currencyRealm); true }
            initRatesChange(this, currencyRealm, context)

            if (currencyRealm.iconBytes == null) {
                downloadAndSaveIcon(icon, currencyRealm, ccList, context)
            } else {
                doAsync {
                    val bitmapIcon = BitmapFactory
                            .decodeStream(java.io.ByteArrayInputStream(currencyRealm.iconBytes))
                    uiThread {
                        icon.setImageBitmap(bitmapIcon)
                    }
                }
            }
        }
    }

    private fun downloadAndSaveIcon(icon: ImageView, currencyRealm: CoinMarketCapCurrencyRealm?,
                                    ccList: MutableList<CryptoCompareCurrencyRealm>, context: Context) {
        val imageLink = CurrencyHelper.getImageLinkForCurrency(currencyRealm!!, ccList)
        if (imageLink.isEmpty()) {
            icon.setImageResource(R.drawable.icon_ico)
        } else {
            Picasso.with(context)
                    .load(Uri.parse(Endpoints.CRYPTO_COMPARE_URL + imageLink))
                    .into(icon, object : Callback {
                        override fun onSuccess() {
                            // TODO Fix saving icons and restoring them in list
//                            doAsync {
//                                val bitmap = (icon.drawable as BitmapDrawable).bitmap
//                                val stream = ByteArrayOutputStream()
//                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                                val byteArray = stream.toByteArray()
//                                presenter.saveCurrencyIcon(currencyRealm, byteArray)
//                            }
                        }

                        override fun onError() {
                            Timber.e("Error downloading icon")
                        }
                    })
        }
    }

    private fun initRatesChange(coinViewHolder: CoinViewHolder,
                                currencyRealm: CoinMarketCapCurrencyRealm?, context: Context) {
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
        var data: CoinMarketCapCurrencyRealm? = null
    }
    // endregion Currency


    // region Simple string item
    class StringViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemRootLayout: LinearLayout = view.find(R.id.simple_item_root)
        var text: TextView = view.find(R.id.item_text)
    }

    fun bindSimpleItem(holder: StringViewHolder, currency: CoinMarketCapCurrencyRealm?,
                       onClick: FindCoinAdapter.SimpleItemClickListener) {
        val text = "${currency?.name} (${currency?.symbol})"
        holder.text.text = text
        holder.itemRootLayout.onClick { onClick.onClick(holder, currency!!) }
    }
    // endregion Simple string item

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

    fun bindPortfolioItem(holder: PortfolioViewHolder, portfolioRealm: PortfolioRealm,
                          context: Context, ccList: MutableList<CryptoCompareCurrencyRealm>,
                          onClick: PortfolioAdapter.OnClick, onLongClick: PortfolioAdapter.OnLongClick) {
        with(holder) {
            symbol.text = portfolioRealm.coin?.symbol
            name.text = portfolioRealm.coin?.name
            amountOfCoins.text = "${portfolioRealm.amountOfCoins} ${portfolioRealm.coin?.symbol}"
            fiatBalance.text = "${AmountFormatter
                    .formatFiatPrice(calculatePortfolioFiatSum(portfolioRealm))} ${context.getString(R.string.usd)}"
            setPortfolioProfit(portfolioRealm, profitPercentage, context)
            itemRootLayout.onClick { onClick.onClick(holder, portfolioRealm) }
            itemRootLayout.onLongClick { onLongClick.onLongClick(holder, portfolioRealm); true }

            downloadAndSaveIcon(icon, portfolioRealm.coin, ccList, context)
        }
    }

    private fun setPortfolioProfit(portfolioRealm: PortfolioRealm, profitPercentage: TextView, context: Context) {
        // Setting value
        if (portfolioRealm.buyPriceInFiat.isNullOrBlank())
            profitPercentage.text = "?"
        else
            profitPercentage.text = calculateProfit(portfolioRealm.coin?.priceUsd,
                    portfolioRealm.buyPriceInFiat?.toDoubleOrNull())

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

    fun calculatePortfolioFiatSum(portfolio: PortfolioRealm) =
            (BigDecimal(portfolio.amountOfCoins) * BigDecimal(portfolio.coin?.priceUsd ?: 0.0))
    // endregion Portfolio
}