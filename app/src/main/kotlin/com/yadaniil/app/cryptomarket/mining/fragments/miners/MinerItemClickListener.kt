package com.yadaniil.app.cryptomarket.mining.fragments.miners

import com.yadaniil.app.cryptomarket.data.api.models.Miner

/**
 * Created by danielyakovlev on 9/20/17.
 */

interface MinerItemClickListener {

    fun onClick(holder: MinersAdapter.MinerViewHolder, tx: Miner)
}