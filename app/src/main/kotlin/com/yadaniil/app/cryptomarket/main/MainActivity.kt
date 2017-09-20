package com.yadaniil.app.cryptomarket.main

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import com.arellomobile.mvp.presenter.InjectPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.base.BaseActivity
import com.yadaniil.app.cryptomarket.data.db.models.CoinMarketCapCurrencyRealm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), IMainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var listDivider: RecyclerView.ItemDecoration

    private lateinit var currenciesAdapter: CurrenciesAdapter

    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        setUpCurrenciesList(presenter.getRealmCurrencies())
        initSearchView()
        presenter.downloadAndSaveAllCurrencies()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_currencies_list, menu)
        val item = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(item)
        return true
    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                setUpCurrenciesList(presenter.getRealmCurrenciesFiltered(newText ?: ""))
                return true
            }

        })
    }

    private fun setUpCurrenciesList(realmCurrencies: RealmResults<CoinMarketCapCurrencyRealm>) {
        currenciesAdapter = CurrenciesAdapter(realmCurrencies, true,
                this, presenter)
        currencies_recycler_view.layoutManager = LinearLayoutManager(this)
        currencies_recycler_view.adapter = currenciesAdapter
        currencies_recycler_view.setHasFixedSize(true)
        currencies_recycler_view.removeItemDecoration(listDivider)
        currencies_recycler_view.addItemDecoration(listDivider)
    }

    override fun onDestroy() {
        super.onDestroy()
        currencies_recycler_view.adapter = null
    }

    override fun showLoading() = smooth_progress_bar.progressiveStart()
    override fun stopLoading() = smooth_progress_bar.progressiveStop()
}
