package com.yadaniil.blogchain.screens.events

import com.yadaniil.blogchain.data.api.models.coindar.CoindarEventResponse

/**
 * Created by danielyakovlev on 1/8/18.
 */

interface EventClickListener {
    fun onClick(holder: EventsAdapter.EventViewHolder, event: CoindarEventResponse)
}