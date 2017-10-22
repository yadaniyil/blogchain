package com.yadaniil.blogchain.mining.fragments.calculator

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.squareup.picasso.Picasso
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.MiningCoin
import com.yadaniil.blogchain.data.api.models.MiningCoinResponse
import com.yadaniil.blogchain.utils.Endpoints
import com.yadaniil.blogchain.utils.UiHelper
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.no_items_layout.*
import kotlinx.android.synthetic.main.profit_table.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import java.math.BigDecimal

/**
 * Created by danielyakovlev on 9/29/17.
 */

class CalculatorFragment : MvpAppCompatFragment(), CalculatorView {

    @InjectPresenter lateinit var presenter: CalculatorPresenter

    // region Fragment
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_calculator, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        UiHelper.changeStatusBarColor(activity, R.color.colorTabCalculator)
        presenter.downloadMiningCoins()
        retry_button.onClick { presenter.downloadMiningCoins() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                activity.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion Fragment

    private fun initToolbar() {
        toolbar.title = getString(R.string.calculator)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    // region View
    override fun initCalculatorView(coins: List<MiningCoin>) {
        calculator_view.visibility = View.VISIBLE
        no_items_layout.visibility = View.GONE

        val coinsForDisplay = coins.map { "${it.name} (${it.tag})" }
        val adapter = ArrayAdapter(activity,
                android.R.layout.simple_spinner_dropdown_item, coinsForDisplay)
        mining_coin_spinner.setTitle(getString(R.string.select_coin))
        mining_coin_spinner.setPositiveButton(getString(R.string.ok))
        mining_coin_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                showCoinIcon()
                changeHashrateExponent()
            }
        }
        mining_coin_spinner.adapter = adapter

        calculate_button.onClick {
            presenter.calculateTable(mining_coin_spinner.selectedItem.toString(),
                    hashrate_edit_text.text.toString(), power_edit_text.text.toString())
        }
    }

    private fun showCoinIcon() {
        val imageLink = presenter.getLinkForCoinImage(mining_coin_spinner.selectedItem.toString())
        if (imageLink.isBlank()) {
            Picasso.with(context)
                    .load(Uri.parse(Endpoints.NICEHASH_ICON))
                    .into(coin_icon)
        } else {
            Picasso.with(context)
                    .load(Uri.parse(Endpoints.CRYPTO_COMPARE_URL + imageLink))
                    .into(coin_icon)
        }
    }

    private fun changeHashrateExponent() {
        val exponent = presenter.getHashrateExponentForCoin(mining_coin_spinner.selectedItem.toString())
        hashrate_exponent_value.text = exponent
    }

    override fun showLoading() {
        no_items_layout.visibility = View.VISIBLE
        downloading_label.visibility = View.VISIBLE
        progress_bar.visibility = View.VISIBLE

        error_image.visibility = View.GONE
        error_message.visibility = View.GONE
        retry_button.visibility = View.GONE
        calculator_view.visibility = View.GONE
    }

    override fun showError() {
        no_items_layout.visibility = View.VISIBLE
        downloading_label.visibility = View.GONE
        progress_bar.visibility = View.GONE

        error_image.visibility = View.VISIBLE
        error_message.visibility = View.VISIBLE
        retry_button.visibility = View.VISIBLE
        calculator_view.visibility = View.GONE
    }

    override fun showTableLoading() {
        table_loading_progress_bar.visibility = View.VISIBLE
        calculated_table.visibility = View.GONE
    }

    override fun showTableError() {
        table_loading_progress_bar.visibility = View.GONE
        calculated_table.visibility = View.GONE
        activity.toast(R.string.error)
    }

    override fun showTable(coin: MiningCoinResponse) {
        table_loading_progress_bar.visibility = View.GONE
        calculated_table.visibility = View.VISIBLE
        val isProfitNegative = coin.profit.startsWith("-")

        val dayReward = BigDecimal(coin.estimatedRewards.replace(",", ""))
        // Substring is used to remove dollar sign
        val dayRewardsDollar = BigDecimal(coin.revenueDollar.substring(1).replace(",", ""))
        val dayCost = BigDecimal(coin.cost.substring(1).replace(",", ""))
        val dayProfit = when {
            coin.profit.startsWith("$") -> BigDecimal(coin.profit.substring(1).replace(",", ""))
            coin.profit.startsWith("-") -> BigDecimal(coin.profit.substring(2).replace(",", ""))
            else -> BigDecimal.ZERO
        }

        // Rewards is removed due to no space in screen

//        hour_rewards.text = "${(dayReward / HOURS_IN_DAY)}"
        hour_revenue_dollar.text = "$${(dayRewardsDollar / HOURS_IN_DAY)}"
        hour_cost.text = "$${(dayCost / HOURS_IN_DAY)}"
        hour_profit.text =
                if (isProfitNegative) "-$${(dayProfit / HOURS_IN_DAY)}"
                else "$${(dayProfit / HOURS_IN_DAY)}"

//        day_rewards.text = coin.estimatedRewards
        day_revenue_dollar.text = coin.revenueDollar
        day_cost.text = coin.cost
        day_profit.text = coin.profit

//        week_rewards.text = "${(dayReward * DAYS_IN_WEEK)}"
        week_revenue_dollar.text = "$${(dayRewardsDollar * DAYS_IN_WEEK)}"
        week_cost.text = "$${(dayCost * DAYS_IN_WEEK)}"
        week_profit.text =
                if (isProfitNegative) "-$${(dayProfit * DAYS_IN_WEEK)}"
                else "$${(dayProfit * DAYS_IN_WEEK)}"

//        month_rewards.text = "${(dayReward * DAYS_IN_MONTH)}"
        month_revenue_dollar.text = "$${(dayRewardsDollar * DAYS_IN_MONTH)}"
        month_cost.text = "$${(dayCost * DAYS_IN_MONTH)}"
        month_profit.text =
                if (isProfitNegative) "-$${(dayProfit * DAYS_IN_MONTH)}"
                else "$${(dayProfit * DAYS_IN_MONTH)}"

//        year_rewards.text = "${(dayReward * DAYS_IN_YEAR)}"
        year_revenue_dollar.text = "$${(dayRewardsDollar * DAYS_IN_YEAR)}"
        year_cost.text = "$${(dayCost * DAYS_IN_YEAR)}"
        year_profit.text =
                if (isProfitNegative) "-$${(dayProfit * DAYS_IN_YEAR)}"
                else "$${(dayProfit * DAYS_IN_YEAR)}"

        calculator_scroll_view.post { calculator_scroll_view.fullScroll(View.FOCUS_DOWN) }
        UiHelper.closeKeyboard(activity)
    }


    // endregion View

    companion object {
        fun newInstance() = CalculatorFragment()
        val HOURS_IN_DAY = BigDecimal(24)
        val DAYS_IN_WEEK = BigDecimal(7)
        val DAYS_IN_MONTH = BigDecimal(30)
        val DAYS_IN_YEAR = DAYS_IN_MONTH * BigDecimal(12)
    }
}