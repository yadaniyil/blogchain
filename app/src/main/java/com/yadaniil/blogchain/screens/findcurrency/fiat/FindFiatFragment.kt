package com.yadaniil.blogchain.screens.findcurrency.fiat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.screens.findcurrency.FindCurrencyActivity
import com.yadaniil.blogchain.screens.findcurrency.events.InitFiatSearchViewEvent
import com.yadaniil.blogchain.screens.findcurrency.fiat.listitems.FiatCurrencyItem
import com.yadaniil.blogchain.screens.findcurrency.fiat.listitems.FiatHeaderItem
import com.yadaniil.blogchain.screens.findcurrency.fiat.listitems.FiatListItem
import com.yadaniil.blogchain.utils.ListHelper
import kotlinx.android.synthetic.main.fragment_find_fiat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

/**
 * Created by danielyakovlev on 11/15/17.
 */

class FindFiatFragment : MvpAppCompatFragment(), FindFiatAdapter.FiatOnClick, FindFiatView {

    @InjectPresenter lateinit var presenter: FindFiatPresenter

    private lateinit var findFiatAdapter: FindFiatAdapter
    private lateinit var searchView: MaterialSearchView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initFiatList()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: InitFiatSearchViewEvent) {
        Timber.e(event.message)
        initSearchView()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    private fun initSearchView() {
        if (searchView.isSearchOpen)
            searchView.closeSearch()

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                initFiatList(newText)
                return true
            }
        })
    }

    private fun initFiatList(newText: String? = "") {
        findFiatAdapter = FindFiatAdapter(getAllFiatCurrencies(newText), activity!!, this)
        fiat_recycler_view.layoutManager = LinearLayoutManager(activity)
        fiat_recycler_view.adapter = findFiatAdapter
        fiat_recycler_view.setHasFixedSize(true)
    }

    private fun getAllFiatCurrencies(newText: String?): MutableList<FiatListItem> {
        val symbols = resources.getStringArray(R.array.fiat_currencies_symbols)
        val names = resources.getStringArray(R.array.fiat_currencies_names)
        val list = listOf(
                FiatHeaderItem(getString(R.string.popular)),
                FiatCurrencyItem(symbols[0], names[0], R.drawable.usd),
                FiatCurrencyItem(symbols[1], names[1], R.drawable.eur),
                FiatCurrencyItem(symbols[2], names[2], R.drawable.gbp),
                FiatCurrencyItem(symbols[3], names[3], R.drawable.jpy),
                FiatCurrencyItem(symbols[4], names[4], R.drawable.rub),

                FiatHeaderItem(getString(R.string.other)),
                FiatCurrencyItem(symbols[5], names[5], R.drawable.aud),
                FiatCurrencyItem(symbols[6], names[6], R.drawable.brl),
                FiatCurrencyItem(symbols[7], names[7], R.drawable.cad),
                FiatCurrencyItem(symbols[8], names[8], R.drawable.chf),
                FiatCurrencyItem(symbols[9], names[9], R.drawable.clp),
                FiatCurrencyItem(symbols[10], names[10], R.drawable.cny),
                FiatCurrencyItem(symbols[11], names[11], R.drawable.czk),
                FiatCurrencyItem(symbols[12], names[12], R.drawable.dkk),
                FiatCurrencyItem(symbols[13], names[13], R.drawable.hkd),
                FiatCurrencyItem(symbols[14], names[14], R.drawable.huf),
                FiatCurrencyItem(symbols[15], names[15], R.drawable.idr),
                FiatCurrencyItem(symbols[16], names[16], R.drawable.ils),
                FiatCurrencyItem(symbols[17], names[17], R.drawable.inr),
                FiatCurrencyItem(symbols[18], names[18], R.drawable.krw),
                FiatCurrencyItem(symbols[19], names[19], R.drawable.mxn),
                FiatCurrencyItem(symbols[20], names[20], R.drawable.myr),
                FiatCurrencyItem(symbols[21], names[21], R.drawable.nok),
                FiatCurrencyItem(symbols[22], names[22], R.drawable.nzd),
                FiatCurrencyItem(symbols[23], names[23], R.drawable.php),
                FiatCurrencyItem(symbols[24], names[24], R.drawable.pkr),
                FiatCurrencyItem(symbols[25], names[25], R.drawable.pln),
                FiatCurrencyItem(symbols[26], names[26], R.drawable.sek),
                FiatCurrencyItem(symbols[27], names[27], R.drawable.sgd),
                FiatCurrencyItem(symbols[28], names[28], R.drawable.thb),
                FiatCurrencyItem(symbols[29], names[29], R.drawable.try_flag),
                FiatCurrencyItem(symbols[30], names[30], R.drawable.twd),
                FiatCurrencyItem(symbols[31], names[31], R.drawable.zar)
        ).toMutableList()

        return if (newText == null || newText.isBlank()) {
            list
        } else {
            var filteredList: MutableList<FiatListItem> = ArrayList()
            filteredList.addAll(list)
            filteredList
                    .filterIsInstance<FiatHeaderItem>()
                    .forEach { filteredList.remove(it) }

            filteredList = filteredList.filter {
                val fiat = it as FiatCurrencyItem
                fiat.symbol.contains(newText, true)
                        || fiat.name.contains(newText, true)
            }.toMutableList()

            filteredList
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_find_fiat, container, false)

    override fun onClick(holder: ListHelper.FindFiatHolder?, fiatItem: FiatCurrencyItem) {
        val returnIntent = Intent()
        returnIntent.putExtra(FindCurrencyActivity.PICKED_COIN_SYMBOL, fiatItem.symbol)
        activity!!.setResult(Activity.RESULT_OK, returnIntent)
        activity!!.finish()
    }

    companion object {
        fun newInstance(search_view: MaterialSearchView): FindFiatFragment {
            val fragment = FindFiatFragment()
            fragment.searchView = search_view
            return fragment
        }
    }
}