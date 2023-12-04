package org.n9ne.h2ohealthy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoApiImpl
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoLocalImpl
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.FragmentCupsBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.dialog.addCupDialog
import org.n9ne.h2ohealthy.ui.profile.adpter.AddCupAdapter
import org.n9ne.h2ohealthy.ui.profile.viewModel.CupsViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Saver.getToken
import org.n9ne.h2ohealthy.util.Utils.isOnline
import org.n9ne.h2ohealthy.util.interfaces.CupClickListener
import org.n9ne.h2ohealthy.util.interfaces.CupEditListener
import org.n9ne.h2ohealthy.util.interfaces.RefreshListener


class CupsFragment : Fragment(), RefreshListener {
    private lateinit var localRepo: ProfileRepoLocalImpl
    private lateinit var apiRepo: ProfileRepoApiImpl

    private var cups = listOf<Cup>()
    private lateinit var b: FragmentCupsBinding
    private lateinit var viewModel: CupsViewModel
    private lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentCupsBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        activity.hideNavigation()

        activity.startLoading()
        makeRequest {
            viewModel.getCups(requireActivity().getToken())
        }
        setupObserver()
    }

    private fun init() {
        activity = requireActivity() as MainActivity

        val client = (requireActivity().application as App).client
        apiRepo = ProfileRepoApiImpl(client)
        localRepo = ProfileRepoLocalImpl(AppDatabase.getDatabase(requireContext()).roomDao())

        viewModel = ViewModelProvider(this)[CupsViewModel::class.java]
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

    private fun setAdapter() {
        b.rvCups.adapter = AddCupAdapter(cups, object : CupEditListener {
            override fun onEdit(cup: Cup, edit: Boolean) {
                if (edit) {
                    requireActivity().addCupDialog(cup, object : CupClickListener {
                        override fun onClick(item: Cup) {

                            activity.startLoading()
                            makeRequest {
                                viewModel.updateCup(item)
                            }
                        }
                    }).show()
                } else {

                    activity.startLoading()
                    makeRequest {
                        viewModel.removeCup(cup)
                    }
                }
            }
        })
    }

    private fun setupObserver() {
        viewModel.ldShowDialog.observe(viewLifecycleOwner, EventObserver {
            requireActivity().addCupDialog(null, object : CupClickListener {
                override fun onClick(item: Cup) {

                    activity.startLoading()
                    makeRequest {
                        viewModel.addCup(item)
                    }
                }
            }).show()
        })
        viewModel.ldCups.observe(viewLifecycleOwner) {
            (requireActivity() as MainActivity).reloadCups(it)
            cups = it
            b.bAdd.isEnabled = true
            setAdapter()
            activity.stopLoading()
        }
        viewModel.ldAddCup.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldRemoveCup.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getCups(requireActivity().getToken())
        }
    }
}