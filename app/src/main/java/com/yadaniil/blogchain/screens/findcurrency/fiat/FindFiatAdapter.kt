package com.yadaniil.blogchain.screens.findcurrency.fiat

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.utils.ListHelper
import org.jetbrains.anko.onClick


/**
 * Created by danielyakovlev on 11/16/17.
 */

class FindFiatAdapter(private val items: MutableList<FiatListItem>, private val context: Context,
                      private val onClick: FiatOnClick)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    interface FiatOnClick {
        fun onClick(holder: ListHelper.FindFiatHolder?, fiatItem: FiatCurrencyItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        return if (viewType == TYPE_HEADER) {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_find_fiat_header, parent, false)
            ListHelper.FiatHeaderHolder(itemView)
        } else {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_find_fiat, parent, false)
            ListHelper.FindFiatHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int) =
            if (items[position] is FiatHeaderItem) TYPE_HEADER else TYPE_ITEM

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is ListHelper.FiatHeaderHolder) {
            holder.text.text = (item as FiatHeaderItem).text
        } else if (holder is ListHelper.FindFiatHolder) {
            val fiat = item as FiatCurrencyItem
            holder.fiatSymbol.text = fiat.symbol
            holder.fiatName.text = fiat.name
            holder.fiatFlagIcon.setImageDrawable(context.resources.getDrawable(item.iconIntRes))
            holder.itemRootLayout.onClick { onClick.onClick(holder, fiat) }
        }
    }

    override fun getItemCount() = items.size


}