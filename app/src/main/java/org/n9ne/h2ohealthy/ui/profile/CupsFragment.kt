package org.n9ne.h2ohealthy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoLocalImpl
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.FragmentCupsBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.dialog.addCupDialog
import org.n9ne.h2ohealthy.ui.profile.adpter.AddCupAdapter
import org.n9ne.h2ohealthy.ui.profile.viewModel.CupsViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Utils.isOnline
import org.n9ne.h2ohealthy.util.interfaces.CupClickListener
import org.n9ne.h2ohealthy.util.interfaces.CupEditListener


class CupsFragment : Fragment() {
    private lateinit var localRepo: ProfileRepoLocalImpl

    private var cups = listOf<Cup>()
    private lateinit var b: FragmentCupsBinding
    private lateinit var viewModel: CupsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentCupsBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).hideNavigation()
        init()

        makeRequest {
            viewModel.getCups()
        }
        setupObserver()
    }

    private fun init() {
        localRepo = ProfileRepoLocalImpl(AppDatabase.getDatabase(requireContext()).roomDao())

        viewModel = ViewModelProvider(this)[CupsViewModel::class.java]
        b.viewModel = viewModel

    }

    private fun makeRequest(request: () -> Unit) {
        val repo = if (requireActivity().isOnline()) {
            //TODO pass apiRepo
            localRepo
        } else {
            localRepo
        }
        viewModel.repo = repo
        request.invoke()
    }

    private fun setAdapter() {
        b.rvCups.adapter = AddCupAdapter(cups, object : CupEditListener {
            override fun onEdit(cup: Cup, edit: Boolean) {
                if (edit) {
                    requireActivity().addCupDialog(cup, object : CupClickListener {
                        override fun onClick(item: Cup) {
                            makeRequest {
                                viewModel.updateCup(item)
                            }
                        }
                    }).show()
                } else {
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
        }
        viewModel.ldAddCup.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldRemoveCup.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }
}