package com.yadaniil.blogchain.screens.news

import com.yadaniil.blogchain.data.api.models.NewsModel
import com.yadaniil.blogchain.utils.ListHelper

/**
 * Created by danielyakovlev on 12/24/17.
 */

interface OnNewsClick {
    fun onClick(holder: ListHelper.NewsHolder, newsModel: NewsModel)
}