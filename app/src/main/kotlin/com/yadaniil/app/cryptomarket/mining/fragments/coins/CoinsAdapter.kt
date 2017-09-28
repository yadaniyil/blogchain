package com.yadaniil.app.cryptomarket.mining.fragments.coins

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.api.models.MiningCoin
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import kotlin.properties.Delegates

/**
 * Created by danielyakovlev on 9/28/17.
 */

class CoinsAdapter(context: Context, coinClickListener: CoinItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var coins: MutableList<MiningCoin>? = ArrayList()
    private var context: Context by Delegates.notNull()
    private var clickListener: CoinItemClickListener by Delegates.notNull()

    init {
        this.context = context
        this.clickListener = coinClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val currentHolder = holder as CoinViewHolder
        val coin = coins!![position]

        currentHolder.name.text = coin.name

        holder.itemLayout.onClick {
            clickListener.onClick(currentHolder, coin)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_mining_coin_list, parent, false)
        return CoinViewHolder(v)
    }

    fun setData(coins: List<MiningCoin>) {
        this.coins?.clear()
        this.coins?.addAll(coins)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return coins?.size!!
    }

    class CoinViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val itemLayout: LinearLayout = v.find(R.id.coin_item_layout)
        val name: TextView = v.find(R.id.name)
    }
}
