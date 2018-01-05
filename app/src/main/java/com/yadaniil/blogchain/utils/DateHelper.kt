package com.yadaniil.blogchain.utils

import android.content.Context
import com.yadaniil.blogchain.R
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by danielyakovlev on 12/23/17.
 */


object DateHelper {
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS

    fun getTimeAgo(date: Date, context: Context) = calculateTimeAgo(date.time, context)

    fun getTimeAgo(time: String, context: Context?): String {
        if (context == null)
            return time

        var longTime = tryParseDate(time).time
        if (longTime < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            longTime *= 1000
        }

        return calculateTimeAgo(longTime, context)
    }

    private fun calculateTimeAgo(longTime: Long, context: Context): String {
        val now = Date().time
        if (longTime > now || longTime <= 0) {
            return Date().toString()
        }

        val diff = now - longTime
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.just_now)
        } else if (diff < 2 * MINUTE_MILLIS) {
            return context.getString(R.string.minute_ago)
        } else if (diff < 50 * MINUTE_MILLIS) {
            return (diff / MINUTE_MILLIS).toString() + " " + context.getString(R.string.minutes_ago)
        } else if (diff < 90 * MINUTE_MILLIS) {
            return context.getString(R.string.hour_ago)
        } else if (diff < 24 * HOUR_MILLIS) {
            return (diff / HOUR_MILLIS).toString() + " " + context.getString(R.string.hours_ago)
        } else if (diff > 24 * HOUR_MILLIS && diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.yesterday)
        } else {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = longTime
            val yearOfTransaction = calendar.get(Calendar.YEAR)

            calendar.timeInMillis = now
            val yearNow = calendar.get(Calendar.YEAR)

            return try {
                if (yearOfTransaction == yearNow) {
                    var dateFormat = SimpleDateFormat("MMMM dd")
                    if(context.getString(R.string.lang_check) == "ru")
                        dateFormat = SimpleDateFormat("d MMMM")
                    val date = Date(longTime)
                    dateFormat.format(date)
                } else {
                    var dateFormat = SimpleDateFormat("MMMM dd, yyyy")
                    if(context.getString(R.string.lang_check) == "ru")
                        dateFormat = SimpleDateFormat("dd MMMM, yyyy")
                    val date = Date(longTime)
                    dateFormat.format(date)
                }
            } catch (e: IllegalArgumentException) {
                Timber.e(e.message)
                ""
            }
        }
    }

    private val supported = arrayOf<DateFormat>(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))

    private fun tryParseDate(strDate: String?): Date {
        if (strDate == null || strDate.isEmpty())
            return Date()
        for (format in supported) {
            try {
                return format.parse(strDate)
            } catch (ignored: ParseException) {
            }

        }
        return Date()
    }
}