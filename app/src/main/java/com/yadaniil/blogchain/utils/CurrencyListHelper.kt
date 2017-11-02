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
import com.yadaniil.blogchain.screens.base.CurrencyClickListener
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.screens.findcoin.FindCoinAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.jetbrains.anko.uiThread
import timber.log.Timber

/**
 * Created by danielyakovlev on 10/31/17.
 */

object CurrencyListHelper {

    // region Currency
    fun bindCurrency(currencyHolder: CurrencyViewHolder, currencyRealm: CoinMarketCapCurrencyRealm,
                     ccList: MutableList<CryptoCompareCurrencyRealm>, context: Context,
                     onClick: CurrencyClickListener, removeRank: Boolean) {
        with(currencyHolder) {
            if(removeRank) rank.visibility = View.GONE else rank.text = currencyRealm.rank.toString()

            data = currencyRealm
            symbol.text = currencyRealm.symbol
            name.text = currencyRealm.name
            usdRate.text = AmountFormatter.format(currencyRealm?.priceUsd ?: "") + " USD"
            btcRate.text = currencyRealm?.priceBtc + " BTC"
            itemRootLayout.onClick { onClick.onClick(currencyHolder, currencyRealm) }
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

    private fun initRatesChange(currencyViewHolder: CurrencyViewHolder,
                                currencyRealm: CoinMarketCapCurrencyRealm?, context: Context) {
        with(currencyViewHolder) {
            hourChange.text = currencyRealm?.percentChange1h + "%"
            dayChange.text = currencyRealm?.percentChange24h + "%"
            weekChange.text = currencyRealm?.percentChange7d + "%"

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

    class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

}