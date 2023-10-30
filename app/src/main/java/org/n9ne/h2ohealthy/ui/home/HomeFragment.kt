package org.n9ne.h2ohealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.Progress
import org.n9ne.h2ohealthy.databinding.FragmentHomeBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.home.adpter.ActivityAdapter
import org.n9ne.h2ohealthy.ui.home.viewModel.HomeViewModel
import org.n9ne.h2ohealthy.util.interfaces.MenuClickListener
import org.nine.linearprogressbar.LinearVerticalProgressBar


class HomeFragment : Fragment() {

    private lateinit var b: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentHomeBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).showNavigation()
        init()

        //TODO set progress bar's
        //TODO set latest activity list

        val daily = viewModel.dailyProgress.toCollection(ArrayList())
        setProgress(viewModel.target, viewModel.progress, daily)

        setActivityAdapter(viewModel.activities)

    }

    private fun init() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        b.viewModel = viewModel
    }

    private fun setActivityAdapter(list: List<Activity>) {
        val adapter = ActivityAdapter(list, object : MenuClickListener {
            override fun onMenuClick(item: Activity) {
                //TODO open options dialog
            }

        })
        b.rvActivity.adapter = adapter
    }

    private fun setProgress(target: Int, progress: Int, daily: ArrayList<Progress>) {
        b.pbTarget.setProgress(progress)
        b.tvTarget.text = target.toString() + "L"

        while (daily.size < 7) {
            daily.add(Progress(0, ""))
        }
        for (i in 0 until b.clProgress.childCount) {
            val v = b.clProgress.getChildAt(i)
            if (v is LinearVerticalProgressBar) {
                v.setProgress(daily[i].progress)
            }
            if (v is TextView) {
                //           v.text = daily[i].day
            }
        }


    }
}