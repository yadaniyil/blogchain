package com.yadaniil.blogchain.screens.watchlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.data.db.models.realm.CoinEntity
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.utils.ListHelper
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 10/31/17.
 */

class WatchlistAdapter(data: RealmResults<CoinEntity>, autoUpdate: Boolean,
                       private var context: Context, val repo: Repository,
                       var onClick: CoinClickListener, var onLongClick: CoinLongClickListener)
    : RealmRecyclerViewAdapter<CoinEntity, ListHelper.CoinViewHolder>(data, autoUpdate) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHelper.CoinViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency, parent, false)
        return ListHelper.CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHelper.CoinViewHolder, position: Int) {
        val currencyRealm = getItem(position)
        ListHelper.bindCoinListItem(holder, currencyRealm!!, repo, context,
                onClick, onLongClick, true)
    }

}