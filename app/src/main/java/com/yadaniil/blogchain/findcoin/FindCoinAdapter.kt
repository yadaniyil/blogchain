package com.yadaniil.blogchain.findcoin

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.utils.CurrencyListHelper
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 11/2/17.
 */

class FindCoinAdapter(data: RealmResults<CoinMarketCapCurrencyRealm>, autoUpdate: Boolean,
                      var onClick: SimpleItemClickListener)
    : RealmRecyclerViewAdapter<CoinMarketCapCurrencyRealm, CurrencyListHelper.StringViewHolder>(data, autoUpdate) {

    interface SimpleItemClickListener {
        fun onClick(holder: CurrencyListHelper.StringViewHolder?, currencyRealm: CoinMarketCapCurrencyRealm)
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyListHelper.StringViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_simple_list, parent, false)
        return CurrencyListHelper.StringViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyListHelper.StringViewHolder?, position: Int) {
        val currencyRealm = getItem(position)
        CurrencyListHelper.bindSimpleItem(holder!!, currencyRealm, onClick)
    }

}