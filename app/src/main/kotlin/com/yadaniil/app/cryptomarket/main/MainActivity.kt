package com.yadaniil.app.cryptomarket.main

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.Application
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.yadaniil.app.cryptomarket.base.BaseActivity
import org.jetbrains.anko.indeterminateProgressDialog


class MainActivity : BaseActivity(), IMainView {

    @Inject
    lateinit var presenter : MainPresenter

    private lateinit var progressDialog: ProgressDialog
    private lateinit var currenciesAdapter: CurrenciesAdapter

    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        setUpCurrenciesList()
        presenter.downloadAndSaveAllCurrencies()
    }

    private fun setUpCurrenciesList() {
        currenciesAdapter = CurrenciesAdapter(presenter.getRealmCurrencies(), true)
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
        progressDialog = indeterminateProgressDialog(message = R.string.loading)
    }

    override fun stopLoading() {
        progressDialog.dismiss()
    }
}
