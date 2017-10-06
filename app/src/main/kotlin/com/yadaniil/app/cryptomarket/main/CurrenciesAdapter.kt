package com.yadaniil.app.cryptomarket.main

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.app.cryptomarket.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.app.cryptomarket.utils.AmountFormatter
import com.yadaniil.app.cryptomarket.utils.CurrencyHelper
import com.yadaniil.app.cryptomarket.utils.Endpoints
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.ByteArrayInputStream


/**
 * Created by danielyakovlev on 7/2/17.
 */

class CurrenciesAdapter(var context: Context, presenter: MainPresenter)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    private var currencies: MutableList<CoinMarketCapCurrencyRealm> = ArrayList()
    private var ccList: MutableList<CryptoCompareCurrencyRealm> = ArrayList()
    var isFastScrolling: Boolean = false


    init {
        ccList = presenter.repo.getAllCryptoCompareCurrenciesFromDb()
    }

    fun getCurrencies() = currencies

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val currencyRealm = currencies[position]
        val currencyHolder = holder as CurrencyViewHolder
        with(currencyHolder) {
            data = currencyRealm
            symbol.text = currencyRealm.symbol
            name.text = currencyRealm.name
            usdRate.text = AmountFormatter.format(currencyRealm?.priceUsd ?: "") + " USD"
            btcRate.text = currencyRealm?.priceBtc + " BTC"
            initRatesChange(this, currencyRealm)
            sortOrder.text = currencyRealm.rank.toString()
            Timber.e("is fast scrolling: " + isFastScrolling)
            if (currencyRealm.iconBytes == null) {
                downloadAndSaveIcon(icon, currencyRealm)
            } else {
                doAsync {
                    val bitmapIcon = BitmapFactory.decodeStream(ByteArrayInputStream(currencyRealm.iconBytes))
                    uiThread {
                        icon.setImageBitmap(bitmapIcon)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    fun setData(currencies: List<CoinMarketCapCurrencyRealm>) {
        this.currencies.clear()
        this.currencies.addAll(currencies)
        notifyDataSetChanged()
    }

    private fun downloadAndSaveIcon(icon: ImageView, currencyRealm: CoinMarketCapCurrencyRealm?) {
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
                                currencyRealm: CoinMarketCapCurrencyRealm?) {
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

    override fun getSectionName(position: Int): String {
        return (position + 1).toString()
    }

    class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var symbol: TextView = view.find(R.id.item_currency_symbol)
        var name: TextView = view.find(R.id.item_currency_name)
        var usdRate: TextView = view.find(R.id.item_currency_price)
        var btcRate: TextView = view.find(R.id.item_currency_btc_price)
        var hourChange: TextView = view.find(R.id.item_currency_hour_change)
        var dayChange: TextView = view.find(R.id.item_currency_day_change)
        var weekChange: TextView = view.find(R.id.item_currency_week_change)
        var icon: ImageView = view.find(R.id.item_currency_icon)
        var sortOrder: TextView = view.find(R.id.item_currency_rank)
        var data: CoinMarketCapCurrencyRealm? = null
    }
}
