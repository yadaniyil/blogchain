package com.yadaniil.blogchain.screens.findcurrency.fiat

import android.support.v4.app.Fragment
import com.miguelcatalan.materialsearchview.MaterialSearchView

/**
 * Created by danielyakovlev on 11/15/17.
 */

class FindFiatFragment : Fragment() {

    private lateinit var searchView: MaterialSearchView

    companion object {
        fun newInstance(search_view: MaterialSearchView): FindFiatFragment {
            val fragment = FindFiatFragment()
            fragment.searchView = search_view
            return fragment
        }
    }
}