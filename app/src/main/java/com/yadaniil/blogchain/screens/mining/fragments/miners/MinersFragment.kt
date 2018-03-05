package com.yadaniil.blogchain.screens.mining.fragments.miners

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.cryptocompare.Miner
import com.yalantis.filter.adapter.FilterAdapter
import com.yalantis.filter.animator.FiltersListItemAnimator
import com.yalantis.filter.listener.FilterListener
import com.yalantis.filter.widget.Filter
import com.yalantis.filter.widget.FilterItem
import kotlinx.android.synthetic.main.fragment_miners.*
import kotlinx.android.synthetic.main.no_items_layout.*
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber
import kotlin.properties.Delegates


/**
 * Created by danielyakovlev on 9/20/17.
 */

class MinersFragment : Fragment(), MinerItemClickListener {

    private val viewModel by viewModel<MinersViewModel>()
    private var minersAdapter by Delegates.notNull<MinersAdapter>()
    private lateinit var drawerAction: () -> Unit

    private lateinit var filter: Filter<MinerFilterTag>
    private lateinit var minerFilterColors: IntArray
    private lateinit var minerFilterNames: Array<String>

    // region Fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_miners, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        minerFilterColors = resources.getIntArray(R.array.miner_filter_colors)
        minerFilterNames = resources.getStringArray(R.array.miner_filters)
        initToolbar()
        retry_button.onClick { viewModel.downloadMiners() }
        showMiners(emptyList())
        viewModel.downloadMiners()
        showFilter()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                drawerAction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion Fragment

    private fun initToolbar() {
        toolbar.title = getString(R.string.miners)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onClick(holder: MinersAdapter.MinerViewHolder, miner: Miner) {

    }

    // region Filtering
    private fun getFilterTags(): List<MinerFilterTag> {
        val tags = ArrayList<MinerFilterTag>()
        (0 until minerFilterNames.size).mapTo(tags) {
            MinerFilterTag(minerFilterNames[it], minerFilterColors[it]) }
        return tags
    }

    private fun showFilter() {
        filter = activity!!.find(R.id.filter)
        filter.adapter = MinersFilterAdapter(getFilterTags(), activity!!, minerFilterColors)
        filter.listener = filterListener
        filter.noSelectedItemText = getString(R.string.all_selected)
        filter.build()
    }

    private val filterListener = object : FilterListener<MinerFilterTag> {
        override fun onFiltersSelected(filters: ArrayList<MinerFilterTag>) {
            val newMiners = viewModel.findMinersByTags(filters)
            try {
                minersAdapter.setData(newMiners)
            } catch (e: IllegalStateException) {
                Timber.e(e.message)
            }

        }
        override fun onNothingSelected() {
            try {
                minersAdapter.setData(viewModel.downloadedMiners)
                minersAdapter.notifyDataSetChanged()
            } catch (e: IllegalStateException) {
                // This will toggle once in onCreate, so we don't show any error
            }

        }
        override fun onFilterSelected(item: MinerFilterTag) {
            if (item.getText() == minerFilterNames[0]) {
                filter.deselectAll()
                filter.collapse()
            }
        }
        override fun onFilterDeselected(item: MinerFilterTag) {

        }
    }

    class MinersFilterAdapter(items: List<MinerFilterTag>, private var context: Context,
                              private var colors: IntArray) : FilterAdapter<MinerFilterTag>(items) {

        override fun createView(position: Int, item: MinerFilterTag): FilterItem {
            val filterItem = FilterItem(context)

            filterItem.strokeColor = colors[0]
            filterItem.textColor = colors[0]
            filterItem.checkedTextColor = ContextCompat.getColor(context, android.R.color.white)
            filterItem.color = ContextCompat.getColor(context, android.R.color.white)
            filterItem.checkedColor = colors[position]
            filterItem.text = item.getText()
            filterItem.deselect()

            return filterItem
        }

    }
    // endregion Filtering

    // region View
    fun showError() {
        downloading_label.visibility = View.GONE
        progress_bar.visibility = View.GONE

        error_image.visibility = View.VISIBLE
        error_message.visibility = View.VISIBLE
        retry_button.visibility = View.VISIBLE
    }

    fun showLoading() {
        downloading_label.visibility = View.VISIBLE
        progress_bar.visibility = View.VISIBLE

        error_image.visibility = View.GONE
        error_message.visibility = View.GONE
        retry_button.visibility = View.GONE
    }

    fun showMiners(miners: List<Miner>) {
        minersAdapter = MinersAdapter(activity!!, this, getFilterTags())
        miners_list.layoutManager = LinearLayoutManager(activity)
        miners_list.adapter = minersAdapter
        miners_list.setHasFixedSize(true)
        miners_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        miners_list.itemAnimator = FiltersListItemAnimator()
        minersAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if(minersAdapter.itemCount > 0) {
                    miners_list.visibility = View.VISIBLE
                    activity?.find<Filter<MinerFilterTag>>(R.id.filter)?.visibility = View.VISIBLE
                    no_items_layout.visibility = View.GONE
                } else {
                    miners_list.visibility = View.GONE
                    activity?.find<Filter<MinerFilterTag>>(R.id.filter)?.visibility = View.INVISIBLE
                    no_items_layout.visibility = View.VISIBLE
                }
            }
        })
        minersAdapter.setData(miners)
    }
    // endregion View

    companion object {
        fun newInstance(openAndCloseDrawerAction: () -> Unit): Fragment {
            val fragment = MinersFragment()
            fragment.drawerAction = openAndCloseDrawerAction
            return fragment
        }
    }
}