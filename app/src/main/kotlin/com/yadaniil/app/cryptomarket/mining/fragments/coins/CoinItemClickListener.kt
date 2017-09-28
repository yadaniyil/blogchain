package com.yadaniil.app.cryptomarket.mining.fragments.coins

import com.yadaniil.app.cryptomarket.data.api.models.MiningCoin

/**
 * Created by danielyakovlev on 9/28/17.
 */


interface CoinItemClickListener {
    fun onClick(holder: CoinsAdapter.CoinViewHolder, coin: MiningCoin)
}