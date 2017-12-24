package com.yadaniil.blogchain.utils

import android.util.Xml
import com.yadaniil.blogchain.data.api.models.NewsModel
import org.xmlpull.v1.XmlPullParser
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import org.xmlpull.v1.XmlPullParserException
import timber.log.Timber
import java.io.IOException
import java.util.*


/**
 * Created by danielyakovlev on 12/10/17.
 */
object XmlParser {

    lateinit var feedSourceTitle: String

    fun parseNewsFeed(rssFeeds: String): List<NewsModel> {
        val inputStream = ByteArrayInputStream(rssFeeds.toByteArray(Charset.forName("UTF-8")))
        inputStream.use { inputStream ->
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<NewsModel> {
        val items: MutableList<NewsModel> = ArrayList()

        parser.require(XmlPullParser.START_TAG, null, "rss")
        var feedSourceLink = ""
        var feedSourceDescription = ""
        var feedSourceImageUrl = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if(name == "channel") continue
            if(name == "title") {
                feedSourceTitle = readText(parser)
                continue
            }
            if(name == "link") {
                feedSourceLink = readText(parser)
                continue
            }
            if(name == "description") {
                feedSourceDescription = readText(parser)
                continue
            }
            if(name == "image") {
                feedSourceImageUrl = readImageUrl(parser)
                continue
            }
            if(name == "item") {
                items.add(readItem(parser))
            } else {
                skip(parser)
            }
        }

        items.forEach {
            it.sourceImageLink = feedSourceImageUrl
            it.sourceName = feedSourceTitle
            if(it.imageLink.isEmpty())
                it.imageLink = feedSourceImageUrl
            if(it.sourceName.contains("cointelegraph", true))
                it.sourceName = "Cointelegraph"
        }

        return items
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    // region Read
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readItem(parser: XmlPullParser): NewsModel {
        parser.require(XmlPullParser.START_TAG, null, "item")
        return when {
            feedSourceTitle.contains("cointelegraph", true) -> parseCointelegraphItem(parser)
            feedSourceTitle.contains("coindesk", true) -> parseCoinDeskItem(parser)
            feedSourceTitle.contains("bitcoin magazine", true) -> parseBitcoinMagazineItem(parser)
            else -> NewsModel("", Date(), "")
        }
    }

    // region Feed parsers
    private fun parseCointelegraphItem(parser: XmlPullParser): NewsModel {
        var title = ""
        var pubDate = Date()
        var link = ""
        var imageUrl = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "title" -> title = readTitle(parser)
                "pubDate" -> pubDate = readPubDate(parser)
                "link" -> link = readText(parser)
                "media:content" -> imageUrl = parseCointelegraphItemImageUrl(parser)
                else -> skip(parser)
            }
        }
        return NewsModel(title, pubDate, link, imageUrl)
    }

    private fun parseCoinDeskItem(parser: XmlPullParser): NewsModel {
        var title = ""
        var pubDate = Date()
        var link = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "title" -> title = readTitle(parser)
                "pubDate" -> pubDate = readPubDate(parser)
                "link" -> link = readText(parser)
                else -> skip(parser)
            }
        }
        return NewsModel(title, pubDate, link)
    }

    private fun parseBitcoinMagazineItem(parser: XmlPullParser): NewsModel {
        var title = ""
        var pubDate = Date()
        var link = ""
        var imageUrl = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "title" -> title = readTitle(parser)
                "pubDate" -> pubDate = readPubDate(parser)
                "link" -> link = readText(parser)
                "description" -> if (imageUrl.isEmpty()) imageUrl = parseBitcoinMagazineItemImageUrl(parser)

                else -> skip(parser)
            }
        }
        return NewsModel(title, pubDate, link, imageUrl)
    }
    // endregion Feed parsers

    private fun parseCointelegraphItemImageUrl(parser: XmlPullParser): String {
        var imageUrl = ""
        parser.require(XmlPullParser.START_TAG, null, "media:content")
        val tag = parser.name
        val relType = parser.getAttributeValue(null, "medium")
        if (tag == "media:content" && relType == "image") {
            imageUrl = parser.getAttributeValue(null, "url")
            parser.nextTag()
        }
        parser.require(XmlPullParser.END_TAG, null, "media:content")
        return imageUrl
    }

    private fun parseBitcoinMagazineItemImageUrl(parser: XmlPullParser): String {
        val imageUrl: String
        val rawDescription = readText(parser)

        imageUrl = rawDescription.substring(
                rawDescription.indexOf("src=\"") + 5, rawDescription.indexOf("\" width="))
        Timber.e("Bitcoin Magazine image url: " + imageUrl)
        return imageUrl
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, "title")
        return title
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readImageUrl(parser: XmlPullParser): String {
        var imageUrl = ""
        parser.require(XmlPullParser.START_TAG, null, "image")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if(name == "url") {
                imageUrl = readText(parser)
            } else skip(parser)
        }
        parser.require(XmlPullParser.END_TAG, null, "image")
        return imageUrl
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLink(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, null, "link")
        val tag = parser.name
        val relType = parser.getAttributeValue(null, "rel")
        if (tag == "link") {
            if (relType == "alternate") {
                link = parser.getAttributeValue(null, "href")
                parser.nextTag()
            }
        }
        parser.require(XmlPullParser.END_TAG, null, "link")
        return link
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readPubDate(parser: XmlPullParser): Date {
        parser.require(XmlPullParser.START_TAG, null, "pubDate")
        val pubDateString = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, "pubDate")
        return Date(pubDateString)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }
    // endregion Read
}