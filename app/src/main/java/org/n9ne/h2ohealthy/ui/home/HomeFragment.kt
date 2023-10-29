package org.n9ne.h2ohealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.MainActivity
import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.databinding.FragmentHomeBinding
import org.n9ne.h2ohealthy.ui.home.adpter.ActivityAdapter
import org.n9ne.h2ohealthy.ui.home.viewModel.HomeViewModel
import org.n9ne.h2ohealthy.util.interfaces.MenuClickListener


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
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        b.viewModel = viewModel
    }

    private fun setAdapter(list: List<Activity>) {
        val adapter = ActivityAdapter(list, object : MenuClickListener {
            override fun onMenuClick(item: Activity) {
                //TODO open options dialog
            }

        })
        b.rvActivity.adapter = adapter
    }
}