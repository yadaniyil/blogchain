package com.yadaniil.blogchain.mining.fragments.miners

import com.yalantis.filter.model.FilterModel

/**
 * Created by danielyakovlev on 9/21/17.
 */
class MinerFilterTag(private var text: String, private var color: Int) : FilterModel {
    override fun getText() = text
    fun getColor() = color
}