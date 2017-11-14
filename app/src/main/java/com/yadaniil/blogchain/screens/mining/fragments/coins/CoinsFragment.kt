package com.yadaniil.blogchain.screens.mining.fragments.coins

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.MiningCoin
import com.yalantis.filter.animator.FiltersListItemAnimator
import kotlinx.android.synthetic.main.fragment_coins.*
import kotlinx.android.synthetic.main.no_items_filtered_layout.*
import kotlinx.android.synthetic.main.no_items_layout.*
import org.jetbrains.anko.onClick
import kotlin.properties.Delegates

/**
 * Created by danielyakovlev on 9/28/17.
 */


class CoinsFragment : MvpAppCompatFragment(), CoinsView, CoinItemClickListener {

    @InjectPresenter lateinit var presenter: CoinsPresenter
    private var coinsAdapter by Delegates.notNull<CoinsAdapter>()
    private lateinit var listDivider: RecyclerView.ItemDecoration
    private lateinit var drawerAction: () -> Unit

    // region Fragment
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_coins, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listDivider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        initToolbar()
        initSearchView()
//        UiHelper.changeStatusBarColor(activity, R.color.colorTabCoins)
        showCoins(emptyList())
        retry_button.onClick { presenter.downloadMiningCoins() }
        presenter.downloadMiningCoins()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                drawerAction()
                true
            }
            R.id.action_info -> {
                showMiningCoinsInfoAlert()
                super.onOptionsItemSelected(item)
            }
//            R.id.action=_filter -> showMiningCoinsFilter()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_mining_coins, menu)
        val item = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(item)
    }
    // endregion Fragment

    private fun initToolbar() {
        toolbar.title = getString(R.string.coins)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun showMiningCoinsInfoAlert() {

    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                showCoins(presenter.getFilteredCoins(newText ?: ""))
                return true
            }

        })
    }

    override fun onClick(holder: CoinsAdapter.CoinViewHolder, coin: MiningCoin) {

    }

    override fun showLoading() {
        downloading_label.visibility = View.VISIBLE
        progress_bar.visibility = View.VISIBLE

        error_image.visibility = View.GONE
        error_message.visibility = View.GONE
        retry_button.visibility = View.GONE
    }

    override fun showError() {
        downloading_label.visibility = View.GONE
        progress_bar.visibility = View.GONE

        error_image.visibility = View.VISIBLE
        error_message.visibility = View.VISIBLE
        retry_button.visibility = View.VISIBLE
    }

    override fun showCoins(coins: List<MiningCoin>) {
        coinsAdapter = CoinsAdapter(activity, this,
                presenter.getAllCmcCurrencies(), presenter.getAllCcCurrencies())
        coins_list.layoutManager = LinearLayoutManager(activity)
        coins_list.adapter = coinsAdapter
        coins_list.setHasFixedSize(true)
        coins_list.removeItemDecoration(listDivider)
        coins_list.addItemDecoration(listDivider)
        coins_list.itemAnimator = FiltersListItemAnimator()
        coinsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if(coinsAdapter.itemCount > 0) {
                    coins_list.visibility = View.VISIBLE
                    no_items_layout.visibility = View.GONE
                    no_items_filtered_layout.visibility = View.GONE
                } else {
                    if(search_view.isSearchOpen) {
                        no_items_layout.visibility = View.GONE
                        coins_list.visibility = View.GONE
                        no_items_filtered_layout.visibility = View.VISIBLE
                    } else {
                        no_items_filtered_layout.visibility = View.GONE
                        no_items_layout.visibility = View.VISIBLE
                        coins_list.visibility = View.GONE
                    }
                }
            }
        })
        coinsAdapter.setData(coins)
    }

    companion object {
        fun newInstance(openAndCloseDrawerAction: () -> Unit): Fragment {
            val fragment = CoinsFragment()
            fragment.drawerAction = openAndCloseDrawerAction
            return fragment
        }
    }
}