package com.yadaniil.blogchain.screens.portfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.utils.CurrencyListHelper
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 11/3/17.
 */


class PortfolioAdapter(data: RealmResults<PortfolioRealm>, autoUpdate: Boolean,
                       private var context: Context,
                       private val ccList: MutableList<CryptoCompareCurrencyRealm>)
    : RealmRecyclerViewAdapter<PortfolioRealm, CurrencyListHelper.PortfolioViewHolder>(data, autoUpdate) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyListHelper.PortfolioViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_portfolio, parent, false)
        return CurrencyListHelper.PortfolioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyListHelper.PortfolioViewHolder?, position: Int) {
        val currencyRealm = getItem(position)
        CurrencyListHelper.bindPortfolioItem(holder!!, currencyRealm!!, context, ccList)
    }
}