package com.yadaniil.blogchain.screens.watchlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.utils.CurrencyListHelper
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 10/31/17.
 */

class WatchlistAdapter(data: RealmResults<CoinMarketCapCurrencyRealm>, autoUpdate: Boolean,
                       private var context: Context, val ccList: MutableList<CryptoCompareCurrencyRealm>,
                       var onClick: CoinClickListener, var onLongClick: CoinLongClickListener)
    : RealmRecyclerViewAdapter<CoinMarketCapCurrencyRealm, CurrencyListHelper.CurrencyViewHolder>(data, autoUpdate) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyListHelper.CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_currency, parent, false)
        return CurrencyListHelper.CurrencyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyListHelper.CurrencyViewHolder?, position: Int) {
        val currencyRealm = getItem(position)
        CurrencyListHelper.bindCurrency(holder!!, currencyRealm!!, ccList, context,
                onClick, onLongClick, true)
    }


}