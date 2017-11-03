package com.yadaniil.blogchain.screens.allcoins

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import org.jetbrains.anko.find
import java.math.BigDecimal

/**
 * Created by danielyakovlev on 10/6/17.
 */
object CoinSorter {

    val FILTER_MARKET_CAP = 0
    val FILTER_COIN_PRICE = 1
    val FILTER_ALPHABETICAL = 2
    val FILTER_VOLUME_24H = 3
    val FILTER_WINNERS_24H = 4
    val FILTER_LOSERS_24H = 5

    private var pickedSortById = R.id.radio_marketcap
    private var pickedSortOrderId = R.id.radio_descending

    fun showCoinSortDialog(activity: AppCompatActivity, adapter: AllCoinsAdapter,
                           colorSortMenuItemInWhite: () -> Unit,
                           colorSortMenuItemInAccent: () -> Unit) {
        val inflater = activity.layoutInflater
        val customView = inflater.inflate(R.layout.dialog_currencies_sort, null)
        val sortByRadioGroup = customView.find<RadioGroup>(R.id.radioGroup_sort_by)
        val sortOrderRadioGroup = customView.find<RadioGroup>(R.id.radioGroup_order)
        val sortOrderLabel = customView.find<TextView>(R.id.sort_order_label)

        // This variables used to save picked sort items only after user
        // presses Apply button
        var localSortById = R.id.radio_marketcap
        var localSortOrderId = R.id.radio_descending

        sortByRadioGroup.setOnCheckedChangeListener { dialog, checkedId ->
            localSortById = checkedId
            if(checkedId == R.id.radio_winners || checkedId == R.id.radio_losers) {
                sortOrderRadioGroup.visibility = View.GONE
                sortOrderLabel.visibility = View.GONE
            } else {
                sortOrderRadioGroup.visibility = View.VISIBLE
                sortOrderLabel.visibility = View.VISIBLE
            }
        }
        sortOrderRadioGroup.setOnCheckedChangeListener { _, checkedId -> localSortOrderId = checkedId }

        sortByRadioGroup.check(pickedSortById)
        sortOrderRadioGroup.check(pickedSortOrderId)

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.sort_by)
        builder.setView(customView)

        builder.setPositiveButton(R.string.apply) { dialog, which ->
            pickedSortById = localSortById
            pickedSortOrderId = localSortOrderId
            if(pickedSortById == R.id.radio_marketcap && pickedSortOrderId == R.id.radio_descending)
                colorSortMenuItemInWhite()
            else colorSortMenuItemInAccent()
            sortCurrencies(adapter) }
        builder.setNegativeButton(R.string.cancel) { dialog, which -> dialog.dismiss() }
        builder.setNeutralButton(R.string.reset) { dialog, which ->
            pickedSortById = R.id.radio_marketcap; pickedSortOrderId = R.id.radio_descending
            sortByRadioGroup.check(R.id.radio_marketcap)
            sortOrderRadioGroup.check(R.id.radio_descending)
            colorSortMenuItemInWhite()
            sortCurrencies(adapter)
        }

        builder.show()
    }

    fun sortCurrencies(adapter: AllCoinsAdapter) {
        val currencies: List<CoinMarketCapCurrencyRealm> = adapter.getCurrencies()
        val isDescending = pickedSortOrderId == R.id.radio_descending
        val sortedCurrencies = when (pickedSortById) {
            R.id.radio_marketcap -> sortByFilter(FILTER_MARKET_CAP, isDescending, currencies)
            R.id.radio_coin_price -> sortByFilter(FILTER_COIN_PRICE, isDescending, currencies)
            R.id.radio_alphabetical -> sortByFilter(FILTER_ALPHABETICAL, isDescending, currencies)
            R.id.radio_24hvolume -> sortByFilter(FILTER_VOLUME_24H, isDescending, currencies)
            R.id.radio_winners -> sortByFilter(FILTER_WINNERS_24H, isDescending, currencies)
            R.id.radio_losers -> sortByFilter(FILTER_LOSERS_24H, isDescending, currencies)
            else -> currencies
        }
        adapter.setData(sortedCurrencies)
    }

    private fun sortByFilter(filter: Int, descending: Boolean,
                             currencies: List<CoinMarketCapCurrencyRealm>): List<CoinMarketCapCurrencyRealm> {
        return if (descending) {
            when (filter) {
                FILTER_MARKET_CAP -> currencies.sortedBy { it.rank }
                FILTER_COIN_PRICE -> currencies.sortedByDescending { BigDecimal(it.priceBtc) }
                FILTER_ALPHABETICAL -> currencies.sortedBy { it.name }
                FILTER_VOLUME_24H -> currencies.sortedByDescending { BigDecimal(it.getVolumeFormatted()) }
                FILTER_WINNERS_24H -> currencies.sortedByDescending { formatPercentageChange(it.percentChange24h) }
                FILTER_LOSERS_24H -> currencies.sortedBy { formatPercentageChange(it.percentChange24h) }
                else -> currencies

            }
        } else {
            when (filter) {
                FILTER_MARKET_CAP -> currencies.sortedByDescending { it.rank }
                FILTER_COIN_PRICE -> currencies.sortedBy { BigDecimal(it.priceBtc) }
                FILTER_ALPHABETICAL -> currencies.sortedByDescending { it.name }
                FILTER_VOLUME_24H -> currencies.sortedBy { BigDecimal(it.getVolumeFormatted()) }
                FILTER_WINNERS_24H -> currencies.sortedByDescending { formatPercentageChange(it.percentChange24h) }
                FILTER_LOSERS_24H -> currencies.sortedBy { formatPercentageChange(it.percentChange24h) }
                else -> currencies

            }
        }
    }

    private fun formatPercentageChange(value: String?): BigDecimal {
        return if(value == null) BigDecimal.ZERO else BigDecimal(value)
    }

}