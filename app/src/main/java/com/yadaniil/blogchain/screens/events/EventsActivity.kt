package com.yadaniil.blogchain.screens.events

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.coindar.CoindarEventResponse
import com.yadaniil.blogchain.screens.base.BaseActivity
import com.yadaniil.blogchain.utils.Navigator
import kotlinx.android.synthetic.main.activity_events.*
import org.jetbrains.anko.toast

/**
 * Created by danielyakovlev on 1/8/18.
 */

class EventsActivity : BaseActivity(), EventsView, EventClickListener {

    @InjectPresenter
    lateinit var presenter: EventsPresenter

    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var listDivider: RecyclerView.ItemDecoration

    override fun getLayout() = R.layout.activity_events


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        initSearchView()
        listDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        setUpEventsList()
        presenter.downloadAllEvents()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_find, menu)
//        val searchItem = menu?.findItem(R.id.action_search)
//        search_view.setMenuItem(searchItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
//        if (search_view.isSearchOpen) {
//            search_view.closeSearch()
//        } else {
            super.onBackPressed()
//        }
    }


//    private fun initSearchView() {
//        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?) = true
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                updateList(viewModel.getFavouriteCoinsFiltered(newText ?: ""))
//                return true
//            }
//        })
//    }

    private fun setUpEventsList() {
        eventsAdapter = EventsAdapter(this, this, presenter.repo)
        events_recycler_view.layoutManager = LinearLayoutManager(this)
        events_recycler_view.adapter = eventsAdapter
        events_recycler_view.itemAnimator = null
        events_recycler_view.setHasFixedSize(true)
        events_recycler_view.removeItemDecoration(listDivider)
        events_recycler_view.addItemDecoration(listDivider)
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun stopLoading() {
        swipe_refresh.isRefreshing = false
    }

    override fun showEvents(events: MutableList<CoindarEventResponse>) {
        eventsAdapter.setData(events)
    }

    override fun showLoadingError() = toast(R.string.error)

    override fun onClick(holder: EventsAdapter.EventViewHolder, event: CoindarEventResponse) {
        Navigator.toWebViewActivity(event.proof, event.caption, this)
    }

}