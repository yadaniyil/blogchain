package com.yadaniil.blogchain.data.api.models

import java.util.*

/**
 * Created by danielyakovlev on 12/6/17.
 */

class NewsModel(var title: String,
                var publishDate: Date,
                var newsLink: String,
                var imageLink: String = "",
                var sourceName: String = "",
                var sourceImageLink: String = "")