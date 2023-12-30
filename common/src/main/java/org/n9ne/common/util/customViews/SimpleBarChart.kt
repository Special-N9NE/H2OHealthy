package org.n9ne.common.util.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.boyzdroizy.simpleandroidbarchart.adapters.BarIntervalsAdapter
import org.n9ne.common.databinding.BarChartBinding
import kotlin.random.Random


class SimpleBarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    BarIntervalsAdapter.IntervalsInterface {

    var max = 0
    lateinit var b: BarChartBinding
    override fun onIntervalSelected(value: String) {
        val list = (1..value.toInt()).map { Random.nextInt(10, 100) }
        initAnalyticsChartAdapter(list.toMutableList())
    }

    private fun initView(context: Context) {
        b = BarChartBinding.inflate(LayoutInflater.from(context), this, true)
    }


    private fun initAnalyticsChartAdapter(items: MutableList<Int>) {
        setMaxValue(items.max() ?: 0)
        val classesAdapter = BarChartAdapter(items, max)
        b.chartRecycler.layoutManager = GridLayoutManager(this.context, items.size)
        b.chartRecycler.adapter = classesAdapter
    }

    private fun initAnalyticsIntervalsAdapter(items: MutableList<Int>) {
        val classesAdapter = BarIntervalsAdapter(items, this)
        b.datesRecycler.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        b.datesRecycler.adapter = classesAdapter
    }

    init {
        initView(context)
    }

    fun setChartData(chartData: MutableList<Int>, intervals: MutableList<Int>) {
        initAnalyticsChartAdapter(chartData)
        initAnalyticsIntervalsAdapter(intervals)
    }

    fun setMaxValue(maxValue: Int) {
        max = maxValue
        b.maxValue.text = (maxValue / 100).toString()
    }

    fun setMinValue(minValue: Int) {
        b.minValue.text = minValue.toString()
    }

    companion object {
        private const val border = 2
    }

}