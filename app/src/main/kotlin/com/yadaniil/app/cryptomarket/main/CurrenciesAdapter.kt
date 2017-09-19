package com.yadaniil.app.cryptomarket.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.app.cryptomarket.utils.CurrencyHelper
import com.yadaniil.app.cryptomarket.utils.Endpoints
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


/**
 * Created by danielyakovlev on 7/2/17.
 */

class CurrenciesAdapter (data: RealmResults<CoinMarketCapCurrencyRealm>, autoUpdate: Boolean,
                         private var context: Context, private var presenter: MainPresenter)
    : RealmRecyclerViewAdapter<CoinMarketCapCurrencyRealm, CurrenciesAdapter.CurrencyViewHolder>(data, autoUpdate) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder?, position: Int) {
        val currencyRealm = getItem(position)
        with(holder ?: return) {
            data = currencyRealm
            symbol.text = currencyRealm?.symbol
            name.text = currencyRealm?.name
            usdRate.text = currencyRealm?.totalSupply
            sortOrder.text = currencyRealm?.rank.toString()
            if (currencyRealm?.iconBytes == null) {
                downloadAndSaveIcon(icon, currencyRealm)
            } else {
                val bitmapIcon = BitmapFactory.decodeStream(ByteArrayInputStream(currencyRealm.iconBytes))
                icon.setImageBitmap(bitmapIcon)
            }
        }
    }

    private fun downloadAndSaveIcon(icon: ImageView, currencyRealm: CoinMarketCapCurrencyRealm?) {
        val ccList = presenter.repo.getAllCryptoCompareCurrenciesFromDb()
        Picasso.with(context)
                .load(Uri.parse(Endpoints.CRYPTO_COMPARE_URL +
                        CurrencyHelper.getImageLinkForCurrency(currencyRealm!!, ccList)))
                .into(icon, object : Callback {
                    override fun onSuccess() {
                        doAsync {
                            val bitmap = (icon.drawable as BitmapDrawable).bitmap
                            val stream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            val byteArray = stream.toByteArray()
                            presenter.saveCurrencyIcon(currencyRealm, byteArray)
                        }
                    }

                    override fun onError() {
                        Timber.e("Error downloading icon")
                    }
                })
    }


    class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var symbol: TextView = view.find(R.id.item_currency_symbol)
        var name: TextView = view.find(R.id.item_currency_name)
        var usdRate: TextView = view.find(R.id.item_currency_usd_rate)
        var icon: ImageView = view.find(R.id.item_currency_icon)
        var sortOrder: TextView = view.find(R.id.item_currency_rank)
        var data: CoinMarketCapCurrencyRealm? = null
    }

}