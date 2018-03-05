package com.yadaniil.blogchain.screens.portfolio

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.PortfolioCoinEntity
import com.yadaniil.blogchain.utils.ListHelper

/**
 * Created by danielyakovlev on 11/3/17.
 */


class PortfolioAdapter(private var context: Context,
                       private val onClick: OnClick, private val onLongClick: OnLongClick)
    : RecyclerView.Adapter<ListHelper.PortfolioViewHolder>() {

    private val items: MutableList<PortfolioCoinEntity> = ArrayList()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHelper.PortfolioViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_portfolio, parent, false)
        return ListHelper.PortfolioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHelper.PortfolioViewHolder, position: Int) {
        val currencyRealm = items[position]
        ListHelper.bindPortfolioItem(holder, currencyRealm, context, onClick, onLongClick)
    }

    interface OnClick {
        fun onClick(holder: ListHelper.PortfolioViewHolder, portfolioCoinEntity: PortfolioCoinEntity)
    }

    interface OnLongClick {
        fun onLongClick(holder: ListHelper.PortfolioViewHolder, portfolioCoinEntity: PortfolioCoinEntity)
    }

    fun setItems(portfolioCoinEntities: List<PortfolioCoinEntity>) {
        items.clear()
        items.addAll(portfolioCoinEntities)
    }
}