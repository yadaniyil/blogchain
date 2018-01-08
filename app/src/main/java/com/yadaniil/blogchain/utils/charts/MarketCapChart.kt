package com.yadaniil.blogchain.utils.charts

import android.content.Context
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.yadaniil.blogchain.R
import com.yadaniil.blogchain.data.api.models.coinmarketcap.CmcMarketCapAndVolumeChartResponse

/**
 * Created by danielyakovlev on 1/8/18.
 */
object MarketCapChart {

    fun initMarketCapChart(chart: LineChart, data: CmcMarketCapAndVolumeChartResponse, context: Context) {
        val entries: MutableList<Entry> = ArrayList()
        (0 until data.marketCaps.size).forEach { i ->
            entries.add(Entry(data.marketCaps[i][0].toFloat(), data.marketCaps[i][1].toFloat()))
        }

        chart.apply {
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false
            setPinchZoom(false)
            description.isEnabled = false

            val markerView = MarkerView(context, R.layout.custom_marker_view)
            markerView.chartView = this
            this.marker = markerView

            val leftAxis = this.axisLeft
            leftAxis.removeAllLimitLines() // reset all limit lines to avoid overlapping lines
//            leftAxis.axisMaximum = 200f
//            leftAxis.axisMinimum = -50f
            //leftAxis.setYOffset(20f);
//            leftAxis.enableGridDashedLine(10f, 10f, 0f)
            leftAxis.setDrawZeroLine(false)

            // limit lines are drawn behind data (and not on top)
            leftAxis.setDrawLimitLinesBehindData(true)

            val l = this.legend
            l.form = Legend.LegendForm.LINE
        }

        val dataSet = LineDataSet(entries, "Label")
        dataSet.color = context.resources.getColor(R.color.colorAccent)
        dataSet.label = context.getString(R.string.market_cap)

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }
}