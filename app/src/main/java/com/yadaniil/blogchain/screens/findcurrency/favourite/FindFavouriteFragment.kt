package com.yadaniil.blogchain.screens.findcurrency.favourite

import android.support.v4.app.Fragment
import com.miguelcatalan.materialsearchview.MaterialSearchView

/**
 * Created by danielyakovlev on 11/15/17.
 */


class FindFavouriteFragment : Fragment() {

    private lateinit var searchView: MaterialSearchView



    companion object {
        fun newInstance(search_view: MaterialSearchView): FindFavouriteFragment {
            val fragment = FindFavouriteFragment()
            fragment.searchView = search_view
            return fragment
        }
    }
}