package org.n9ne.h2ohealthy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoApiImpl
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoLocalImpl
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.FragmentStatsBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.profile.viewModel.StatsViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Saver.getToken
import org.n9ne.h2ohealthy.util.Utils.isOnline
import org.n9ne.h2ohealthy.util.interfaces.RefreshListener
import kotlin.math.roundToInt


class StatsFragment : Fragment(), RefreshListener {

    private lateinit var localRepo: ProfileRepoLocalImpl
    private lateinit var apiRepo: ProfileRepoApiImpl

    private lateinit var b: FragmentStatsBinding
    private lateinit var viewModel: StatsViewModel

    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentStatsBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).hideNavigation()
        init()

        setClicks()
        setupObserver()

        activity.startLoading()
        makeLocalRequest {
            viewModel.getActivities(requireActivity().getToken())
        }
    }


    private fun makeRequest(request: () -> Unit) {
        val repo = if (requireActivity().isOnline()) {
            apiRepo
        } else {
            localRepo
        }
        viewModel.repo = repo
        request.invoke()
    }

    private fun makeLocalRequest(request: () -> Unit) {
        viewModel.repo = localRepo
        request.invoke()
    }

    private fun makeApiRequest(request: () -> Unit) {
        viewModel.repo = apiRepo
        request.invoke()
    }

    private fun init() {
        activity = requireActivity() as MainActivity
        val client = (requireActivity().application as App).client

        apiRepo = ProfileRepoApiImpl(client)
        localRepo = ProfileRepoLocalImpl(AppDatabase.getDatabase(requireContext()).roomDao())

        viewModel = ViewModelProvider(this)[StatsViewModel::class.java]
        viewModel.db = AppDatabase.getDatabase(requireContext())
        b.viewModel = viewModel

    }


    private fun setupObserver() {

        viewModel.ldBarData.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            setBars(it)
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setClicks() {
        //TODO
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

    override fun onRefresh() {
        //TODO
    }
}