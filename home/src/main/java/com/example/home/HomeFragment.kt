package com.example.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.home.adpter.ActivityAdapter
import com.example.home.databinding.FragmentHomeBinding
import com.example.home.repo.HomeRepo
import com.example.home.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.common.BaseFragment
import org.n9ne.common.dialog.activityOptionDialog
import org.n9ne.common.model.Activity
import org.n9ne.common.model.Progress
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Mapper.toLiter
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.interfaces.AddWaterListener
import org.n9ne.common.util.interfaces.MenuClickListener
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.common.util.interfaces.RemoveActivityListener
import org.nine.linearprogressbar.LinearVerticalProgressBar

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeRepo>(), RefreshListener {
    private lateinit var b: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private var activityList = arrayListOf<Activity>()

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

        baseActivity.showNavigation()

        if (getToken() == null) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("h2ohealthy://auth")
            )
            requireActivity().startActivity(intent)
            requireActivity().finish()
        } else {

            baseActivity.startLoading()
            makeRequest {
                viewModel.getTarget(getToken())
            }
        }
    }

    private fun init() {

        viewModel.db = db

        b.viewModel = viewModel

        initRepos(apiRepo, localRepo, viewModel)
    }

    private fun setActivityAdapter(list: List<Activity>) {
        val adapter = ActivityAdapter(list, object : MenuClickListener {
            override fun onMenuClick(item: Activity) {
                val editListener = object : AddWaterListener {
                    override fun onAdd(amount: String) {
                        item.amount = amount.toDouble().toLiter().toString()

                        baseActivity.startLoading()
                        makeApiRequest {
                            viewModel.updateWater(item, getToken())
                        }
                    }
                }
                val removeListener = object : RemoveActivityListener {
                    override fun onRemove() {

                        baseActivity.startLoading()
                        makeApiRequest {
                            viewModel.removeWater(item, getToken())
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
                viewModel.getProgress(getToken())
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

            baseActivity.stopLoading()
        }
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getTarget(getToken())
        }
    }
}