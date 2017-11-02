package com.yadaniil.blogchain.screens.mining.fragments.miners

import com.yadaniil.blogchain.data.api.models.Miner

/**
 * Created by danielyakovlev on 9/20/17.
 */

interface MinerItemClickListener {

    fun onClick(holder: MinersAdapter.MinerViewHolder, tx: Miner)
}