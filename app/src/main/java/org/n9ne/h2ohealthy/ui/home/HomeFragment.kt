package org.n9ne.h2ohealthy.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Progress
import org.n9ne.h2ohealthy.data.repo.home.HomeRepoApiImpl
import org.n9ne.h2ohealthy.data.repo.home.HomeRepoLocalImpl
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.FragmentHomeBinding
import org.n9ne.h2ohealthy.ui.AuthActivity
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.dialog.activityOptionDialog
import org.n9ne.h2ohealthy.ui.home.adpter.ActivityAdapter
import org.n9ne.h2ohealthy.ui.home.viewModel.HomeViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Mapper.toLiter
import org.n9ne.h2ohealthy.util.Saver.getToken
import org.n9ne.h2ohealthy.util.Utils.isOnline
import org.n9ne.h2ohealthy.util.interfaces.AddWaterListener
import org.n9ne.h2ohealthy.util.interfaces.MenuClickListener
import org.n9ne.h2ohealthy.util.interfaces.RefreshListener
import org.n9ne.h2ohealthy.util.interfaces.RemoveActivityListener
import org.nine.linearprogressbar.LinearVerticalProgressBar


class HomeFragment : Fragment(), RefreshListener {
    private lateinit var localRepo: HomeRepoLocalImpl
    private lateinit var apiRepo: HomeRepoApiImpl

    private lateinit var b: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var activityList = arrayListOf<Activity>()
    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentHomeBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setObservers()

        activity.showNavigation()

        if (requireActivity().getToken() == null) {
            requireActivity().startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        } else {

            activity.startLoading()
            makeRequest {
                viewModel.getTarget(requireActivity().getToken())
            }
        }
    }

    private fun init() {
        activity = requireActivity() as MainActivity
        val client = (requireActivity().application as App).client

        apiRepo = HomeRepoApiImpl(client)
        localRepo = HomeRepoLocalImpl(AppDatabase.getDatabase(requireContext()).roomDao())
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.db = AppDatabase.getDatabase(requireContext())

        b.viewModel = viewModel
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

    private fun makeApiRequest(request: () -> Unit) {
        viewModel.repo = apiRepo
        request.invoke()
    }
    private fun makeLocalRequest(request: () -> Unit) {
        viewModel.repo = localRepo
        request.invoke()
    }

    private fun setActivityAdapter(list: List<Activity>) {
        val adapter = ActivityAdapter(list, object : MenuClickListener {
            override fun onMenuClick(item: Activity) {
                val editListener = object : AddWaterListener {
                    override fun onAdd(amount: String) {
                        item.amount = amount.toDouble().toLiter().toString()

                        activity.startLoading()
                        makeApiRequest {
                            viewModel.updateWater(item)
                        }
                    }
                }
                val removeListener = object : RemoveActivityListener {
                    override fun onRemove() {

                        activity.startLoading()
                        makeApiRequest {
                            viewModel.removeWater(item)
                        }
                    }
                }
                val dialog =
                    requireActivity().activityOptionDialog(item, editListener, removeListener)
                dialog.show()
            }
        })
        b.rvActivity.adapter = adapter
    }

    private fun setProgress(daily: List<Progress>) {
        for ((index, i) in (0 until b.clProgress.childCount step 2).withIndex()) {
            val v = b.clProgress.getChildAt(i)
            (v as LinearVerticalProgressBar).setProgress(daily[index].progress)
        }
        for ((index, i) in (1 until b.clProgress.childCount step 2).withIndex()) {
            val v = b.clProgress.getChildAt(i)
            (v as TextView).text = daily[index].day
        }
    }

    private fun setObservers() {
        viewModel.ldTarget.observe(viewLifecycleOwner) {
            b.tvTarget.text = it.toString() + "L"

            makeRequest {
                viewModel.getProgress(requireActivity().getToken())
            }
        }
        viewModel.ldDayProgress.observe(viewLifecycleOwner) {
            b.pbTarget.setProgress(it)
        }
        viewModel.ldWeekProgress.observe(viewLifecycleOwner) {
            setProgress(it)
        }
        viewModel.ldActivities.observe(viewLifecycleOwner) {
            activityList = it.toCollection(ArrayList())
            setActivityAdapter(it)

            activity.stopLoading()
        }
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getTarget(requireActivity().getToken())
        }
    }
}