package com.yadaniil.app.cryptomarket.mining.fragments.miners

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.api.models.Miner
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import java.util.ArrayList
import kotlin.properties.Delegates

/**
 * Created by danielyakovlev on 9/20/17.
 */

class MinersAdapter(context: Context, minerClickListener: MinerItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var miners: MutableList<Miner>? = ArrayList()
    private var context: Context by Delegates.notNull()
    private var clickListener: MinerItemClickListener by Delegates.notNull()

    init {
        this.context = context
        this.clickListener = minerClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val currentHolder = holder as MinerViewHolder
        val currentMiner = miners!![position]

        holder.name.text = currentMiner.name

        holder.itemLayout.onClick {
            clickListener.onClick(currentHolder, currentMiner)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_miner_list, parent, false)
        return MinerViewHolder(v)
    }

    fun setData(miners: MutableList<Miner>) {
        this.miners?.clear()
        this.miners?.addAll(miners)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return miners?.size!!
    }

    fun addMiner(miner: Miner) {
        miners?.add(0, miner)
        notifyItemInserted(0)
    }

    class MinerViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val itemLayout: LinearLayout = v.find(R.id.miner_item_layout)
        val name: TextView = v.find(R.id.name)
    }
}
