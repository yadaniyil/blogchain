package com.yadaniil.app.cryptomarket.main

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.db.models.CurrencyRealm
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import org.jetbrains.anko.find
import android.view.LayoutInflater
import android.widget.ImageView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber


/**
 * Created by danielyakovlev on 7/2/17.
 */

class CurrenciesAdapter constructor(data: OrderedRealmCollection<CurrencyRealm>?, autoUpdate: Boolean,
                                    private var context: Context)
    : RealmRecyclerViewAdapter<CurrencyRealm, CurrenciesAdapter.CurrencyViewHolder>(data, autoUpdate) {

    init { setHasStableIds(true) }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder?, position: Int) {
        val currencyRealm = getItem(position)
        with(holder!!) {
            data = currencyRealm
            name.text = currencyRealm?.name
            usdRate.text = currencyRealm?.priceUsd
            rank.text = currencyRealm?.rank.toString()
            loadIconToCurrencyItem(currencyRealm, holder)
        }
    }

    private fun loadIconToCurrencyItem(currencyRealm: CurrencyRealm?, holder: CurrencyViewHolder?) {
        val symbol = currencyRealm?.symbol
        val resourceId = context.resources?.getIdentifier(
                symbol.toString().toLowerCase(),
                "drawable", context.packageName)
        try {
            val icon = context.resources?.getDrawable(resourceId!!)
            holder?.icon?.background = icon
        } catch (e: Exception) {
            holder?.icon?.background = null
            Timber.e(e.message)
        }
    }

    class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.find<TextView>(R.id.item_currency_name)
        var usdRate: TextView = view.find<TextView>(R.id.item_currency_usd_rate)
        var icon: ImageView = view.find<ImageView>(R.id.item_currency_icon)
        var rank: TextView = view.find<TextView>(R.id.item_currency_rank)
        var data: CurrencyRealm? = null
    }
}