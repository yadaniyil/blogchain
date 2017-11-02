package com.yadaniil.blogchain.findcoin

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.db.models.CoinMarketCapCurrencyRealm
import com.yadaniil.blogchain.utils.CurrencyListHelper
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_find_coin.*
import kotlinx.android.synthetic.main.no_items_filtered_layout.*
import kotlinx.android.synthetic.main.no_items_layout.*
import android.app.Activity
import android.content.Intent



/**
 * Created by danielyakovlev on 11/2/17.
 */

class FindCoinActivity : MvpAppCompatActivity(), FindCoinView, FindCoinAdapter.SimpleItemClickListener {

    @InjectPresenter
    lateinit var presenter: FindCoinPresenter

    private lateinit var findCoinAdapter: FindCoinAdapter
    private lateinit var listDivider: RecyclerView.ItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_coin)
        listDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        initToolbar()
        initSearchView()
        initCoinList(presenter.getAllRealmCurrencies())
    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                initCoinList(presenter.getRealmCurrenciesFiltered(newText ?: ""))
                return true
            }
        })
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.pick_favourite_coin)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_find, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(searchItem)
        return true
    }

    override fun onBackPressed() {
        if (search_view.isSearchOpen) {
            search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    private fun initCoinList(currencies: RealmResults<CoinMarketCapCurrencyRealm>) {
        findCoinAdapter = FindCoinAdapter(currencies, true, this)
        currencies_recycler_view.layoutManager = LinearLayoutManager(this)
        currencies_recycler_view.adapter = findCoinAdapter
        currencies_recycler_view.setHasFixedSize(true)
        currencies_recycler_view.removeItemDecoration(listDivider)
        currencies_recycler_view.addItemDecoration(listDivider)
        findCoinAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (findCoinAdapter.itemCount > 0) {
                    currencies_recycler_view.visibility = View.VISIBLE
                    no_items_layout.visibility = View.GONE
                    no_items_filtered_layout.visibility = View.GONE
                } else {
                    currencies_recycler_view.visibility = View.GONE
                    if(search_view.isSearchOpen) {
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

    override fun onClick(holder: CurrencyListHelper.StringViewHolder?, currencyRealm: CoinMarketCapCurrencyRealm) {
        val returnIntent = Intent()
        returnIntent.putExtra(PICKED_COIN_SYMBOL, currencyRealm.symbol)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    companion object {
        val PICKED_COIN_SYMBOL = "picked_coin_symbol"
    }
}