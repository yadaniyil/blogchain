package com.yadaniil.app.cryptomarket.mining.fragments.miners

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.api.models.Miner
import com.yadaniil.app.cryptomarket.utils.Endpoints
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import java.util.ArrayList
import kotlin.properties.Delegates
import android.graphics.drawable.GradientDrawable



/**
 * Created by danielyakovlev on 9/20/17.
 */

class MinersAdapter(context: Context, minerClickListener: MinerItemClickListener, private val filterTags: List<MinerFilterTag>)
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
        Picasso.with(context).load(
                Uri.parse(Endpoints.CRYPTO_COMPARE_URL + currentMiner.logoUrl))
                .into(holder.image)

        holder.equipmentType.text = currentMiner.equipmentType
        holder.algorithm.text = currentMiner.algorithm
        holder.hashSpeed.text = currentMiner.hashesPerSecond

//        val drawable = GradientDrawable()
//        drawable.cornerRadius = 1000f
//        drawable.setColor(findMinerEquipmentColor(currentMiner))
//        holder.equipmentType.setBackgroundDrawable(drawable)
//
//        val drawable = GradientDrawable()
//        drawable.cornerRadius = 1000f
//        drawable.setColor(findMinerAlgorithmColor(currentMiner))
//        holder.equipmentType.setBackgroundDrawable(drawable)

        holder.itemLayout.onClick {
            clickListener.onClick(currentHolder, currentMiner)
        }
    }

//    private fun findMinerAlgorithmColor(currentMiner: Miner): Int {
//        filterTags.find { it.getText() == currentMiner.algorithm }
//    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_miner_list, parent, false)
        return MinerViewHolder(v)
    }

    fun setData(miners: List<Miner>) {
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
        val image: ImageView = v.find(R.id.miner_image)
        val name: TextView = v.find(R.id.name)
        val algorithm: TextView = v.find(R.id.algorithm)
        val equipmentType: TextView = v.find(R.id.equipment_type)
        val hashSpeed: TextView = v.find(R.id.hash_speed)
    }
}
