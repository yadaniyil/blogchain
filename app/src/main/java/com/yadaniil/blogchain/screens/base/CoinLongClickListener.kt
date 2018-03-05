package com.yadaniil.blogchain.screens.base

import com.yadaniil.blogchain.data.db.models.CoinEntity
import com.yadaniil.blogchain.utils.ListHelper

/**
 * Created by danielyakovlev on 11/3/17.
 */

interface CoinLongClickListener {
    fun onLongClick(holder: ListHelper.CoinViewHolder, coinEntity: CoinEntity)
}