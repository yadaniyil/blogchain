package com.yadaniil.app.blogchain.mining.fragments.coins

import com.yadaniil.app.blogchain.data.api.models.MiningCoin

/**
 * Created by danielyakovlev on 9/28/17.
 */


interface CoinItemClickListener {
    fun onClick(holder: CoinsAdapter.CoinViewHolder, coin: MiningCoin)
}