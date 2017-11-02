package com.yadaniil.blogchain.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.base.CurrencyClickListener
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.utils.CurrencyListHelper


/**
 * Created by danielyakovlev on 7/2/17.
 */

class CurrenciesAdapter(var context: Context, presenter: MainPresenter, val onClick: CurrencyClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    private var currencies: MutableList<CoinMarketCapCurrencyRealm> = ArrayList()
    private var ccList: MutableList<CryptoCompareCurrencyRealm> = ArrayList()

    init {
        ccList = presenter.repo.getAllCryptoCompareCurrenciesFromDb()
    }

    fun getCurrencies() = currencies

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val currencyRealm = currencies[position]
        val currencyHolder = holder as CurrencyListHelper.CurrencyViewHolder
        CurrencyListHelper.bindCurrency(currencyHolder, currencyRealm, ccList, context, onClick, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_currency, parent, false)
        return CurrencyListHelper.CurrencyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    fun setData(currencies: List<CoinMarketCapCurrencyRealm>) {
        this.currencies.clear()
        this.currencies.addAll(currencies)
        notifyDataSetChanged()
    }

    override fun getSectionName(position: Int): String {
        return (position + 1).toString()
    }
}
