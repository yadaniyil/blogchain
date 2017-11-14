package com.yadaniil.blogchain.utils

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Created by danielyakovlev on 11/3/17.
 */

class AmountInputFilter(digitsBeforeZero: Int?, digitsAfterZero: Int?) : InputFilter {
    private val digitsBeforeZero: Int
    private val digitsAfterZero: Int
    private val pattern: Pattern

    init {
        this.digitsBeforeZero = digitsBeforeZero ?: DIGITS_BEFORE_ZERO_DEFAULT
        this.digitsAfterZero = digitsAfterZero ?: DIGITS_AFTER_ZERO_DEFAULT
        pattern = Pattern.compile("-?[0-9]{0," + this.digitsBeforeZero + "}+((\\.[0-9]{0," + this.digitsAfterZero
                + "})?)||(\\.)?")
    }

    override fun filter(source: CharSequence, start: Int, end: Int,
                        dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        val replacement = source.subSequence(start, end).toString()
        val newVal = dest.subSequence(0, dstart).toString() + replacement +
                dest.subSequence(dend, dest.length).toString()
        val matcher = pattern.matcher(newVal)
        if (matcher.matches())
            return null

        return if (TextUtils.isEmpty(source))
            dest.subSequence(dstart, dend)
        else
            ""
    }

    companion object {

        private val DIGITS_BEFORE_ZERO_DEFAULT = 6
        private val DIGITS_AFTER_ZERO_DEFAULT = 2
    }
}