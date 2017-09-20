package com.yadaniil.app.cryptomarket.mining.fragments.miners

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.data.api.models.Miner
import kotlinx.android.synthetic.main.fragment_miners.*
import kotlin.properties.Delegates

/**
 * Created by danielyakovlev on 9/20/17.
 */

class MinersFragment : MvpAppCompatFragment(), MinersView, MinerItemClickListener {

    @InjectPresenter lateinit var presenter: MinersPresenter
    private var minersAdapter by Delegates.notNull<MinersAdapter>()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_miners, container, false)
        setHasOptionsMenu(true)
        return rootView
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMinersList()
        initToolbar()
        presenter.downloadMiners()
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

    private fun initMinersList() {
        minersAdapter = MinersAdapter(activity, this)
        miners_list.layoutManager = LinearLayoutManager(activity)
        miners_list.adapter = minersAdapter
        miners_list.setHasFixedSize(true)
        miners_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    override fun onClick(holder: MinersAdapter.MinerViewHolder, tx: Miner) {

    }

    override fun showMiners(miners: List<Miner>) {
        minersAdapter.setData(miners.toMutableList())
    }

    companion object {
        fun newInstance() = MinersFragment()
    }
}