package com.yadaniil.blogchain.screens.portfolio

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CryptoCompareCurrencyRealm
import com.yadaniil.blogchain.data.db.models.PortfolioRealm
import com.yadaniil.blogchain.utils.AmountFormatter
import com.yadaniil.blogchain.utils.CurrencyListHelper
import com.yadaniil.blogchain.utils.CurrencyListHelper.calculatePortfolioFiatSum
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import java.math.BigDecimal

/**
 * Created by danielyakovlev on 11/3/17.
 */


class PortfolioAdapter(data: RealmResults<PortfolioRealm>, autoUpdate: Boolean,
                       private var context: Context,
                       private val ccList: MutableList<CryptoCompareCurrencyRealm>,
                       private val onUpdateCallback: () -> Unit)
    : RealmRecyclerViewAdapter<PortfolioRealm, CurrencyListHelper.PortfolioViewHolder>(data, autoUpdate) {

    init {
        setHasStableIds(true)
    }

    override fun updateData(data: OrderedRealmCollection<PortfolioRealm>?) {
        super.updateData(data)
        onUpdateCallback()
    }





    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CurrencyListHelper.PortfolioViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_portfolio, parent, false)
        return CurrencyListHelper.PortfolioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyListHelper.PortfolioViewHolder?, position: Int) {
        val currencyRealm = getItem(position)
        CurrencyListHelper.bindPortfolioItem(holder!!, currencyRealm!!, context, ccList)
    }

    fun getPortfolioSumFiatFormatted(): String {
        var sum: BigDecimal = BigDecimal.ZERO
        data!!.forEach {
            sum += calculatePortfolioFiatSum(it)
        }
        return "${AmountFormatter.formatFiatPrice(sum)} USD"
    }

}