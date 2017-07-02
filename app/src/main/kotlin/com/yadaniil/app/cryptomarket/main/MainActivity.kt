package com.yadaniil.app.cryptomarket.main

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.yadaniil.app.cryptomarket.Application
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), IMainView {

    @Inject
    lateinit var presenter : MainPresenter

    private lateinit var currenciesAdapter: CurrenciesAdapter

    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        setUpCurrenciesList()
        presenter.downloadAndSaveAllCurrencies()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_currencies_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpCurrenciesList() {
        currenciesAdapter = CurrenciesAdapter(presenter.getRealmCurrencies(), true,
                this, presenter)
        currencies_recycler_view.layoutManager = LinearLayoutManager(this)
        currencies_recycler_view.adapter = currenciesAdapter
        currencies_recycler_view.setHasFixedSize(true)
        currencies_recycler_view.addItemDecoration(
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onDestroy() {
        super.onDestroy()
        currencies_recycler_view.adapter = null
    }

    private fun inject() = DaggerMainComponent.builder()
            .applicationComponent(Application.component)
            .mainModule(MainModule(this))
            .build().inject(this)

    override fun showLoading() {
        smooth_progress_bar.progressiveStart()
    }

    override fun stopLoading() {
        smooth_progress_bar.progressiveStop()
    }
}
