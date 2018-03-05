package com.yadaniil.blogchain.screens.allcoins

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinEntity
import org.jetbrains.anko.find

/**
 * Created by danielyakovlev on 11/7/17.
 */

object AllCoinsHelper {

    lateinit var coins: List<CoinEntity>
    lateinit var viewModel: AllCoinsViewModel

    // region CoinSorter
    private val FILTER_MARKET_CAP = 0
    private val FILTER_COIN_PRICE = 1
    private val FILTER_ALPHABETICAL = 2
    private val FILTER_VOLUME_24H = 3
    private val FILTER_WINNERS_24H = 4
    private val FILTER_LOSERS_24H = 5

    private var pickedSortById = R.id.radio_marketcap
    private var pickedSortOrderId = R.id.radio_descending

    fun showCoinSortDialog(activity: AppCompatActivity, adapter: AllCoinsAdapter,
                           colorSortMenuItemInWhite: () -> Unit,
                           colorSortMenuItemInAccent: () -> Unit,
                           initNewAdapter: () -> Unit) {
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
            if (checkedId == R.id.radio_winners || checkedId == R.id.radio_losers) {
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
            if (pickedSortById == R.id.radio_marketcap && pickedSortOrderId == R.id.radio_descending)
                colorSortMenuItemInWhite()
            else colorSortMenuItemInAccent()
            sortCurrencies(initNewAdapter)
        }
        builder.setNegativeButton(R.string.cancel) { dialog, which -> dialog.dismiss() }
        builder.setNeutralButton(R.string.reset) { dialog, which ->
            pickedSortById = R.id.radio_marketcap; pickedSortOrderId = R.id.radio_descending
            sortByRadioGroup.check(R.id.radio_marketcap)
            sortOrderRadioGroup.check(R.id.radio_descending)
            colorSortMenuItemInWhite()
            sortCurrencies(initNewAdapter)
        }

        builder.show()
    }

    private fun sortCurrencies(initNewAdapter: () -> Unit) {
        val isDescending = pickedSortOrderId == R.id.radio_descending
        val sortedCurrencies = when (pickedSortById) {
            R.id.radio_marketcap -> sortByFilter(FILTER_MARKET_CAP, isDescending, coins)
            R.id.radio_coin_price -> sortByFilter(FILTER_COIN_PRICE, isDescending, coins)
            R.id.radio_alphabetical -> sortByFilter(FILTER_ALPHABETICAL, isDescending, coins)
            R.id.radio_24hvolume -> sortByFilter(FILTER_VOLUME_24H, isDescending, coins)
            R.id.radio_winners -> sortByFilter(FILTER_WINNERS_24H, isDescending, coins)
            R.id.radio_losers -> sortByFilter(FILTER_LOSERS_24H, isDescending, coins)
            else -> coins
        }

        AllCoinsHelper.coins = sortedCurrencies
        initNewAdapter()
    }

    private fun sortByFilter(filter: Int, descending: Boolean,
                             currencies: List<CoinEntity>)
            : List<CoinEntity> {
        return if (descending) {
            when (filter) {
                FILTER_MARKET_CAP -> viewModel.getAllCoinsSorted("marketCapUsd", true)
                FILTER_COIN_PRICE -> viewModel.getAllCoinsSorted("priceUsd", true)
                FILTER_ALPHABETICAL -> viewModel.getAllCoinsSorted("name", false)
                FILTER_VOLUME_24H -> viewModel.getAllCoinsSorted("volume24hUsd", true)
                FILTER_WINNERS_24H -> viewModel.getAllCoinsSorted("percentChange24h", true)
                FILTER_LOSERS_24H -> viewModel.getAllCoinsSorted("percentChange24h", false)
                else -> currencies
            }
        } else {
            when (filter) {
                FILTER_MARKET_CAP -> viewModel.getAllCoinsSorted("marketCapUsd", false)
                FILTER_COIN_PRICE -> viewModel.getAllCoinsSorted("priceUsd", false)
                FILTER_ALPHABETICAL -> viewModel.getAllCoinsSorted("name", true)
                FILTER_VOLUME_24H -> viewModel.getAllCoinsSorted("volume24hUsd", false)
                FILTER_WINNERS_24H -> viewModel.getAllCoinsSorted("percentChange24h", true)
                FILTER_LOSERS_24H -> viewModel.getAllCoinsSorted("percentChange24h", false)
                else -> currencies
            }
        }
    }
    // endregion CoinSorter
}