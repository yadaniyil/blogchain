package com.yadaniil.blogchain.screens.findcurrency.crypto

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.utils.ListHelper
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 11/2/17.
 */

class FindCoinAdapter(data: RealmResults<CoinMarketCapCurrencyRealm>, autoUpdate: Boolean,
                      var onClick: SimpleItemClickListener,
                      val ccList: MutableList<CryptoCompareCurrencyRealm>, val context: Context)
    : RealmRecyclerViewAdapter<CoinMarketCapCurrencyRealm, ListHelper.FindCoinHolder>(data, autoUpdate) {

    interface SimpleItemClickListener {
        fun onClick(holder: ListHelper.FindCoinHolder?, currencyRealm: CoinMarketCapCurrencyRealm)
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListHelper.FindCoinHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_find_coin, parent, false)
        return ListHelper.FindCoinHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHelper.FindCoinHolder?, position: Int) {
        val currencyRealm = getItem(position)
        ListHelper.bindFindCoin(holder!!, currencyRealm, onClick, ccList, context)
    }

}