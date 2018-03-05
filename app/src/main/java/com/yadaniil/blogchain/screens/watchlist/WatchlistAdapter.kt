package com.yadaniil.blogchain.screens.watchlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.utils.ListHelper

/**
 * Created by danielyakovlev on 10/31/17.
 */

class WatchlistAdapter(private var context: Context, var onClick: CoinClickListener,
                       var onLongClick: CoinLongClickListener)
    : RecyclerView.Adapter<ListHelper.CoinViewHolder>() {

    private val coins: MutableList<CoinEntity> = ArrayList()

    override fun getItemCount() = coins.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHelper.CoinViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency, parent, false)
        return ListHelper.CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHelper.CoinViewHolder, position: Int) {
        val currencyRealm = coins[position]
        ListHelper.bindCoinListItem(holder, currencyRealm, context, onClick, onLongClick, true)
    }

    fun updateData(newCoins: List<CoinEntity>) {
        coins.clear()
        coins.addAll(newCoins)
    }

}