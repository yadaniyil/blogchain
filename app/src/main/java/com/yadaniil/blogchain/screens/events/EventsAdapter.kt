package com.yadaniil.blogchain.screens.events

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.coindar.CoindarEventResponse
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.utils.ImageLoader
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.listeners.onClick
import kotlin.properties.Delegates

/**
 * Created by danielyakovlev on 1/8/18.
 */


class EventsAdapter(context: Context, onClickListener: EventClickListener,
                    private val coins: List<CoinEntity>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var events: MutableList<CoindarEventResponse>? = ArrayList()
    private var context: Context by Delegates.notNull()
    private var clickListener: EventClickListener by Delegates.notNull()

    init {
        this.context = context
        this.clickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentHolder = holder as EventViewHolder
        val currentEvent = events!![position]

        holder.eventTitle.text = currentEvent.caption
        holder.eventType.text = "Event type"
        holder.currencyName.text = currentEvent.coinName
        holder.currencyTicker.text = currentEvent.coinSymbol

        val coin = coins.find { it.symbol == currentEvent.coinSymbol }
        ImageLoader.loadCoinIcon(coin, holder.currencyIcon, context)

        holder.eventDate.text = currentEvent.startDate
        val drawableForEquipmentType = GradientDrawable()
        drawableForEquipmentType.cornerRadius = 500f
        drawableForEquipmentType.setColor(context.resources.getColor(R.color.colorPrimary))
        holder.eventDate.setBackgroundDrawable(drawableForEquipmentType)

        holder.itemLayout.onClick {
            clickListener.onClick(currentHolder, currentEvent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(v)
    }

    fun setData(events: List<CoindarEventResponse>) {
        this.events?.clear()
        this.events?.addAll(events)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return events?.size!!
    }

    class EventViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val itemLayout: LinearLayout = v.find(R.id.event_item_layout)
        val currencyIcon: ImageView = v.find(R.id.coin_icon)
        val currencyName: TextView = v.find(R.id.item_currency_name)
        val currencyTicker: TextView = v.find(R.id.item_currency_symbol)
        val eventTitle: TextView = v.find(R.id.event_title)
        val eventDate: TextView = v.find(R.id.event_date)
        val eventType: TextView = v.find(R.id.event_type)
    }
}