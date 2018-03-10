package com.yadaniil.blogchain.screens.allcoins

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.screens.base.CoinClickListener
import com.yadaniil.blogchain.screens.base.CoinLongClickListener
import com.yadaniil.blogchain.utils.ListHelper


/**
 * Created by danielyakovlev on 7/2/17.
 */

class AllCoinsAdapter(private var onClick: CoinClickListener,
                      private var onLongClick: CoinLongClickListener,
                      private var context: Context)
    : RecyclerView.Adapter<ListHelper.CoinViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    var data: MutableList<CoinEntity> = ArrayList()

    override fun getItemCount() = data.size

    override fun getSectionName(position: Int): String {
        return (position + 1).toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHelper.CoinViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency, parent, false)
        return ListHelper.CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHelper.CoinViewHolder, position: Int) {
        val coin = data[position]
        ListHelper.bindCoinListItem(holder, coin, context, onClick, onLongClick, false)
    }

    fun setCoins(coins: List<CoinEntity>) {
        data = coins.toMutableList()
        notifyDataSetChanged()
    }
}
