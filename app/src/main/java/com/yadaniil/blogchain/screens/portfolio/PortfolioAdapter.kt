package com.yadaniil.blogchain.screens.portfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.Repository
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.utils.ListHelper
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by danielyakovlev on 11/3/17.
 */


class PortfolioAdapter(data: RealmResults<PortfolioRealm>, autoUpdate: Boolean,
                       private var context: Context,
                       private val repo: Repository,
                       private val onClick: OnClick, private val onLongClick: OnLongClick)
    : RealmRecyclerViewAdapter<PortfolioRealm, ListHelper.PortfolioViewHolder>(data, autoUpdate) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListHelper.PortfolioViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_portfolio, parent, false)
        return ListHelper.PortfolioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListHelper.PortfolioViewHolder?, position: Int) {
        val currencyRealm = getItem(position)
        ListHelper.bindPortfolioItem(holder!!, currencyRealm!!, context, repo, onClick, onLongClick)
    }

    interface OnClick {
        fun onClick(holder: ListHelper.PortfolioViewHolder, portfolioItem: PortfolioRealm)
    }

    interface OnLongClick {
        fun onLongClick(holder: ListHelper.PortfolioViewHolder, portfolioItem: PortfolioRealm)
    }
}