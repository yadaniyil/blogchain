package com.yadaniil.app.cryptomarket.mining.fragments.miners

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.api.models.Miner
import com.yalantis.filter.adapter.FilterAdapter
import com.yalantis.filter.listener.FilterListener
import com.yalantis.filter.widget.Filter
import com.yalantis.filter.widget.FilterItem
import kotlinx.android.synthetic.main.fragment_miners.*
import org.jetbrains.anko.find
import kotlin.properties.Delegates
import com.yalantis.filter.animator.FiltersListItemAnimator
import com.yadaniil.app.cryptomarket.utils.UiHelper


/**
 * Created by danielyakovlev on 9/20/17.
 */

class MinersFragment : MvpAppCompatFragment(), MinersView, MinerItemClickListener {

    @InjectPresenter lateinit var presenter: MinersPresenter
    private var minersAdapter by Delegates.notNull<MinersAdapter>()

    private lateinit var filter: Filter<MinerFilterTag>
    private lateinit var minerFilterColors: IntArray
    private lateinit var minerFilterNames: Array<String>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_miners, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        minerFilterColors = resources.getIntArray(R.array.miner_filter_colors)
        minerFilterNames = resources.getStringArray(R.array.miner_filters)
        initToolbar()
        UiHelper.changeStatusBarColor(activity, R.color.colorPrimary)
        presenter.downloadMiners()
        showFilter()
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.miners)
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

    override fun onClick(holder: MinersAdapter.MinerViewHolder, tx: Miner) {

    }

    override fun showMiners(miners: List<Miner>) {
        minersAdapter = MinersAdapter(activity, this, getFilterTags())
        miners_list.layoutManager = LinearLayoutManager(activity)
        miners_list.adapter = minersAdapter
        miners_list.setHasFixedSize(true)
        miners_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        miners_list.itemAnimator = FiltersListItemAnimator()
        minersAdapter.setData(miners)
    }

    // region Filtering
    private fun getFilterTags(): List<MinerFilterTag> {
        val tags = ArrayList<MinerFilterTag>()
        (0 until minerFilterNames.size).mapTo(tags) {
            MinerFilterTag(minerFilterNames[it], minerFilterColors[it]) }
        return tags
    }

    private fun showFilter() {
        filter = activity.find(R.id.filter)
        filter.adapter = MinersFilterAdapter(getFilterTags(), activity, minerFilterColors)
        filter.listener = filterListener
        filter.noSelectedItemText = getString(R.string.all_selected)
        filter.build()
    }

    private val filterListener = object : FilterListener<MinerFilterTag> {
        override fun onFiltersSelected(filters: ArrayList<MinerFilterTag>) {
            val newMiners = presenter.findMinersByTags(filters)
            minersAdapter.setData(newMiners)
        }
        override fun onNothingSelected() {
            try {
                minersAdapter.setData(presenter.downloadedMiners)
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

    companion object {
        fun newInstance() = MinersFragment()
    }
}