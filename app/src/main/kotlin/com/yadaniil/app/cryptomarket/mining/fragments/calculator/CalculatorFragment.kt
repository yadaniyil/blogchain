package com.yadaniil.app.cryptomarket.mining.fragments.calculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.yadaniil.app.cryptomarket.R
import com.yadaniil.app.cryptomarket.utils.UiHelper
import kotlinx.android.synthetic.main.fragment_calculator.*

/**
 * Created by danielyakovlev on 9/29/17.
 */

class CalculatorFragment : MvpAppCompatFragment(), CalculatorView {

    @InjectPresenter lateinit var presenter: CalculatorPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_calculator, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        UiHelper.changeStatusBarColor(activity, R.color.colorTabCalculator)
    }

    private fun initToolbar() {
        toolbar.title = getString(R.string.calculator)
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


    companion object {
        fun newInstance() = CalculatorFragment()
    }
}