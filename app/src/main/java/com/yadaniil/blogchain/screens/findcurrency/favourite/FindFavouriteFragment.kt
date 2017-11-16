package com.yadaniil.blogchain.screens.findcurrency.favourite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.screens.findcurrency.FindCurrencyActivity.Companion.PICKED_COIN_SYMBOL
import com.yadaniil.blogchain.screens.findcurrency.crypto.FindCoinAdapter
import com.yadaniil.blogchain.utils.ListHelper
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_find_favourite.*
import kotlinx.android.synthetic.main.no_items_filtered_layout.*
import kotlinx.android.synthetic.main.no_items_layout.*
import timber.log.Timber

/**
 * Created by danielyakovlev on 11/15/17.
 */


class FindFavouriteFragment : MvpAppCompatFragment(), FindCoinAdapter.SimpleItemClickListener, FindFavouriteView {

    @InjectPresenter lateinit var presenter: FindFavouritePresenter

    private lateinit var findCoinAdapter: FindCoinAdapter
    private lateinit var searchView: MaterialSearchView


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSearchView()
        initCoinList(presenter.getFavouriteCoins())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_find_favourite, container, false)

    private fun initSearchView() {
        if(searchView.isSearchOpen)
            searchView.closeSearch()
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.e(newText)
                initCoinList(presenter.getFavouriteCoinsFiltered(newText ?: ""))
                return true
            }
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if(isVisibleToUser)
            initSearchView()
    }

    private fun initCoinList(currencies: RealmResults<CoinMarketCapCurrencyRealm>) {
        findCoinAdapter = FindCoinAdapter(currencies, true, this, presenter.getCcCoins(), activity)
        favourites_recycler_view.layoutManager = LinearLayoutManager(activity)
        favourites_recycler_view.adapter = findCoinAdapter
        favourites_recycler_view.setHasFixedSize(true)
        findCoinAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (findCoinAdapter.itemCount > 0) {
                    favourites_recycler_view.visibility = View.VISIBLE
                    no_items_layout.visibility = View.GONE
                    no_items_filtered_layout.visibility = View.GONE
                } else {
                    favourites_recycler_view.visibility = View.GONE
                    if (searchView.isSearchOpen) {
                        no_items_layout.visibility = View.GONE
                        no_items_filtered_layout.visibility = View.VISIBLE
                    } else {
                        no_items_filtered_layout.visibility = View.GONE
                        no_items_layout.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onClick(holder: ListHelper.FindCoinHolder?, currencyRealm: CoinMarketCapCurrencyRealm) {
        val returnIntent = Intent()
        returnIntent.putExtra(PICKED_COIN_SYMBOL, currencyRealm.symbol)
        activity.setResult(Activity.RESULT_OK, returnIntent)
        activity.finish()
    }

    companion object {
        fun newInstance(search_view: MaterialSearchView): FindFavouriteFragment {
            val fragment = FindFavouriteFragment()
            fragment.searchView = search_view
            return fragment
        }
    }
}