package com.yadaniil.blogchain.screens.findcurrency.crypto

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.screens.findcurrency.FindCurrencyActivity.Companion.PICKED_COIN_SYMBOL
import com.yadaniil.blogchain.screens.findcurrency.events.InitCoinsSearchViewEvent
import com.yadaniil.blogchain.utils.ListHelper
import kotlinx.android.synthetic.main.fragment_find_cryptocurrencies.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber

/**
 * Created by danielyakovlev on 11/15/17.
 */

class FindCoinFragment : Fragment(), ListHelper.OnCoinClickListener {

    private val viewModel by viewModel<FindCoinViewModel>()

    private lateinit var findCoinAdapter: FindCoinAdapter
    private lateinit var searchView: MaterialSearchView
    private lateinit var allCoins: List<CoinEntity>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        allCoins = viewModel.getAllCoins()
        initCoinList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_find_cryptocurrencies, container, false)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: InitCoinsSearchViewEvent) {
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
        if(searchView.isSearchOpen)
            searchView.closeSearch()

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                findCoinAdapter.updateData(viewModel.getAllCoinsFiltered(newText ?: ""))
                return true
            }
        })
    }

    private fun initCoinList() {
        findCoinAdapter = FindCoinAdapter(this, activity!!)
        currencies_recycler_view.layoutManager = LinearLayoutManager(activity)
        currencies_recycler_view.adapter = findCoinAdapter
        currencies_recycler_view.setHasFixedSize(true)
    }

    override fun onClick(holder: ListHelper.FindCoinHolder?, currencyRealm: CoinEntity) {
        val returnIntent = Intent()
        returnIntent.putExtra(PICKED_COIN_SYMBOL, currencyRealm.symbol)
        activity!!.setResult(Activity.RESULT_OK, returnIntent)
        activity!!.finish()
    }

    companion object {
        fun newInstance(search_view: MaterialSearchView, initSearchImmediately: Boolean): FindCoinFragment {
            val fragment = FindCoinFragment()
            fragment.searchView = search_view
            if(initSearchImmediately) fragment.initSearchView()
            return fragment
        }
    }


}