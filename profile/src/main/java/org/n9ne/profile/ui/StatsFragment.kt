package org.n9ne.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.common.BaseActivity
import org.n9ne.common.BaseFragment
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.profile.databinding.FragmentStatsBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.ui.viewModel.StatsViewModel
import kotlin.math.roundToInt


@AndroidEntryPoint
class StatsFragment : BaseFragment<ProfileRepo>(), RefreshListener {

    private lateinit var b: FragmentStatsBinding
    private val viewModel: StatsViewModel by viewModels()

    private lateinit var chartOverall: AAChartModel
    private lateinit var chartMonth: AAChartModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentStatsBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as BaseActivity).hideNavigation()
        init()

        setupObserver()

        baseActivity.startLoading()
        makeLocalRequest {
            viewModel.getActivities(getToken())
        }
    }

    private fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)


        val colorInt =
            ContextCompat.getColor(requireContext(), org.n9ne.common.R.color.linearBlueEnd)
        val color = String.format("#%06X", 0xFFFFFF and colorInt)
        val chartModel = AAChartModel()
            .colorsTheme(listOf(color).toTypedArray())
            .chartType(AAChartType.Areaspline)
            .markerRadius(0)
            .tooltipEnabled(false)
            .gradientColorEnable(true)
            .legendEnabled(false)
            .touchEventEnabled(false)
            .yAxisGridLineWidth(0)
            .yAxisTitle("")
            .xAxisVisible(false)
            .dataLabelsEnabled(false)
        chartOverall = chartModel
        chartMonth = chartModel

    }


    private fun setupObserver() {

        viewModel.ldAverage.observe(viewLifecycleOwner, EventObserver {
            b.tvAverage.text = "$it L"
        })
        viewModel.ldScore.observe(viewLifecycleOwner, EventObserver {
            b.tvScore.text = "$it"
        })
        viewModel.ldBarData.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()


            b.tvEmpty.visibility = View.GONE
            b.clStats.visibility = View.VISIBLE

            setBars(it)
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
        viewModel.ldStartDate.observe(viewLifecycleOwner, EventObserver {
            b.tvDateAll.text = it
        })
        viewModel.ldLineOverall.observe(viewLifecycleOwner, EventObserver {
            setLineChart(it, b.lineOverall, chartOverall)
        })
        viewModel.ldEmpty.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            b.tvEmpty.visibility = View.VISIBLE
            b.clStats.visibility = View.INVISIBLE
        })
        viewModel.ldLineMonth.observe(viewLifecycleOwner, EventObserver {
            setLineChart(it, b.lineMonth, chartMonth)
        })
    }

    private fun setBars(list: List<Double>) {
        val chartData = list.map {
            it * 100
        }.map {
            it.roundToInt()
        }.toMutableList()

        b.barChart.setMaxValue(list.max().roundToInt() * 100)
        b.barChart.setMinValue(0)

        b.barChart.setChartData(chartData, mutableListOf())
    }

    private fun setLineChart(it: List<Double>, view: AAChartView, chart: AAChartModel) {
        val arr: Array<Any> = it.toTypedArray()
        chart
            .yAxisMax(it.max())
            .yAxisMin(it.min())
            .series(
                arrayOf(
                    AASeriesElement()
                        .data(arr)
                )
            )
        view.aa_drawChartWithChartModel(chart)
    }

    override fun onRefresh() {
        makeRequest {
            viewModel.getActivities(getToken())
        }
    }
}