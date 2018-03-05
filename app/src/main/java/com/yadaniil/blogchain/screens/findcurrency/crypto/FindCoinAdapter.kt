package com.yadaniil.blogchain.screens.findcurrency.crypto

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.utils.ListHelper

/**
 * Created by danielyakovlev on 11/2/17.
 */

class FindCoinAdapter(var onClick: ListHelper.OnCoinClickListener, val context: Context)
    : RecyclerView.Adapter<ListHelper.FindCoinHolder>() {

    private val coins: MutableList<CoinEntity> = ArrayList()

    override fun getItemCount() = coins.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHelper.FindCoinHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_find_coin, parent, false)
        return ListHelper.FindCoinHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHelper.FindCoinHolder, position: Int) {
        val currencyRealm = coins[position]
        ListHelper.bindFindCoin(holder, currencyRealm, onClick, context)
    }

    fun updateData(newCoins: List<CoinEntity>) {
        coins.clear()
        coins.addAll(newCoins)
    }

}