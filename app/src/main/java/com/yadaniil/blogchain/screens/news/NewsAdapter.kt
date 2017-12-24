package com.yadaniil.blogchain.screens.news

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.NewsModel
import com.yadaniil.blogchain.utils.DateHelper
import com.yadaniil.blogchain.utils.ImageLoader
import com.yadaniil.blogchain.utils.ListHelper
import org.jetbrains.anko.onClick

/**
 * Created by danielyakovlev on 12/23/17.
 */


class NewsAdapter(private val context: Context,
                  private val onNewsClick: OnNewsClick) : RecyclerView.Adapter<ListHelper.NewsHolder>() {

    private val items: MutableList<NewsModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListHelper.NewsHolder {
        val view =  LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_news, parent, false)
        return ListHelper.NewsHolder(view)
    }

    override fun onBindViewHolder(holder: ListHelper.NewsHolder?, position: Int) {
        val item = items[position]
        holder?.title?.text = item.title
        holder?.sourceName?.text = item.sourceName
        holder?.pubDate?.text = DateHelper.getTimeAgo(item.publishDate, context)
        holder?.rootView?.onClick { onNewsClick.onClick(holder, item) }
        ImageLoader.load(item.imageLink, holder?.image!!, context)
    }

    override fun getItemCount() = items.size

    fun updateNews(news: List<NewsModel>) {
        this.items.clear()
        this.items.addAll(news)
    }
}