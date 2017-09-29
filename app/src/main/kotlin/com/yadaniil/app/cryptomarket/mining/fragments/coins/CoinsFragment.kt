package com.yadaniil.app.cryptomarket.mining.fragments.coins

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.api.models.MiningCoin
import com.yadaniil.app.cryptomarket.utils.UiHelper
import com.yalantis.filter.animator.FiltersListItemAnimator
import kotlinx.android.synthetic.main.fragment_coins.*
import kotlin.properties.Delegates

/**
 * Created by danielyakovlev on 9/28/17.
 */


class CoinsFragment : MvpAppCompatFragment(), CoinsView, CoinItemClickListener {

    @InjectPresenter lateinit var presenter: CoinsPresenter
    private var coinsAdapter by Delegates.notNull<CoinsAdapter>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_coins, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        UiHelper.changeStatusBarColor(activity, R.color.colorTabCoins)
        presenter.downloadMiningCoins()
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.coins)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
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

    override fun showCoins(coins: List<MiningCoin>) {
        coinsAdapter = CoinsAdapter(activity, this,
                presenter.getAllCmcCurrencies(), presenter.getAllCcCurrencies())
        coins_list.layoutManager = LinearLayoutManager(activity)
        coins_list.adapter = coinsAdapter
        coins_list.setHasFixedSize(true)
        coins_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        coins_list.itemAnimator = FiltersListItemAnimator()
        coinsAdapter.setData(coins)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_mining_coins, menu)
    }

    override fun onClick(holder: CoinsAdapter.CoinViewHolder, coin: MiningCoin) {

    }


    companion object {
        fun newInstance() = CoinsFragment()
    }
}