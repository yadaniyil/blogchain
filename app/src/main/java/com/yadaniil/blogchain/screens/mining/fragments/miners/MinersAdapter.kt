package com.yadaniil.blogchain.screens.mining.fragments.miners

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
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.Miner
import com.yadaniil.blogchain.utils.Endpoints
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
        holder.company.text = currentMiner.company
        Picasso.with(context).load(
                Uri.parse(Endpoints.CRYPTO_COMPARE_URL + currentMiner.logoUrl))
                .into(holder.image)

        holder.equipmentType.text = currentMiner.equipmentType
        holder.algorithm.text = currentMiner.algorithm
        holder.hashSpeed.text = getHumanReadableHashSpeed(currentMiner.hashesPerSecond)
        when {
            currentMiner.currency == context.getString(R.string.usd) -> holder.price.text = "$ " + currentMiner.cost
            currentMiner.currency == context.getString(R.string.btc) -> holder.price.text = currentMiner.cost + " Ƀ"
            else -> holder.price.text = currentMiner.cost + currentMiner.currency
        }

        if(currentMiner.powerConsumption.endsWith("W"))
            holder.power.text = currentMiner.powerConsumption
        else
            holder.power.text = currentMiner.powerConsumption + "W"

        val drawableForEquipmentType = GradientDrawable()
        drawableForEquipmentType.cornerRadius = 500f
        drawableForEquipmentType.setColor(findMinerEquipmentColor(currentMiner))
        holder.equipmentType.setBackgroundDrawable(drawableForEquipmentType)

        val drawableForAlgorithm = GradientDrawable()
        drawableForAlgorithm.cornerRadius = 500f
        drawableForAlgorithm.setColor(findMinerAlgorithmColor(currentMiner))
        holder.algorithm.setBackgroundDrawable(drawableForAlgorithm)

        holder.itemLayout.onClick {
            clickListener.onClick(currentHolder, currentMiner)
        }
    }

    private fun getHumanReadableHashSpeed(hashSpeed: String): String {
        if(hashSpeed.endsWith("000000000")) {
            return hashSpeed.substring(0, hashSpeed.indexOf("000000000")) + " GH/s"
        }
        if(hashSpeed.endsWith("000000")) {
            return hashSpeed.substring(0, hashSpeed.indexOf("000000")) + " MH/s"
        }
        if(hashSpeed.endsWith("00000")) {
            val valueWithoutComma = hashSpeed.substring(0, hashSpeed.indexOf("00000"))
            return valueWithoutComma.substring(0, valueWithoutComma.length - 1) +
                    "," + valueWithoutComma.last() + " MH/s"
        }

        return hashSpeed + " H/s"
    }

    private fun findMinerEquipmentColor(currentMiner: Miner): Int {
        return filterTags.find { it.getText() == currentMiner.equipmentType }?.getColor() ?: 0
    }

    private fun findMinerAlgorithmColor(currentMiner: Miner): Int {
        return filterTags.find { it.getText() == currentMiner.algorithm }?.getColor() ?: 0
    }

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
        val company: TextView = v.find(R.id.company)
        val algorithm: TextView = v.find(R.id.algorithm)
        val equipmentType: TextView = v.find(R.id.equipment_type)
        val hashSpeed: TextView = v.find(R.id.hash_speed)
        val power: TextView = v.find(R.id.power)
        val price: TextView = v.find(R.id.price)
    }
}
