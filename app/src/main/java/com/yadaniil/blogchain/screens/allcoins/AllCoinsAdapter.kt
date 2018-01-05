package com.yadaniil.blogchain.screens.allcoins

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.utils.ListHelper
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults


/**
 * Created by danielyakovlev on 7/2/17.
 */

class AllCoinsAdapter(data: RealmResults<CoinMarketCapCurrencyRealm>, autoUpdate: Boolean,
                      private var context: Context, val repo: Repository,
                      var onClick: CoinClickListener, var onLongClick: CoinLongClickListener)
    : RealmRecyclerViewAdapter<CoinMarketCapCurrencyRealm, ListHelper.CoinViewHolder>(data, autoUpdate),
        FastScrollRecyclerView.SectionedAdapter {

    var data: RealmResults<CoinMarketCapCurrencyRealm>? = null


    init {
        setHasStableIds(true)
    }

    override fun getSectionName(position: Int): String {
        return (position + 1).toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListHelper.CoinViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_currency, parent, false)
        return ListHelper.CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHelper.CoinViewHolder?, position: Int) {
        val currencyRealm = getItem(position)
        ListHelper.bindCurrency(holder!!, currencyRealm!!, repo, context,
                onClick, onLongClick, false)
    }
}
