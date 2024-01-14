package org.n9ne.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.common.BaseActivity
import org.n9ne.common.BaseFragment
import org.n9ne.common.dialog.addCupDialog
import org.n9ne.common.model.Cup
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.interfaces.CupClickListener
import org.n9ne.common.util.interfaces.CupEditListener
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.profile.databinding.FragmentCupsBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.ui.adpter.AddCupAdapter
import org.n9ne.profile.ui.viewModel.CupsViewModel


@AndroidEntryPoint
class CupsFragment : BaseFragment<ProfileRepo>(), RefreshListener {
    private var cups = listOf<Cup>()
    private lateinit var b: FragmentCupsBinding
    private val viewModel: CupsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentCupsBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        baseActivity.hideNavigation()

        baseActivity.startLoading()
        makeRequest {
            viewModel.getCups(getToken())
        }
        setupObserver()
    }

    private fun init() {
        viewModel.db = db

        b.viewModel = viewModel

        initRepos(apiRepo, localRepo, viewModel)

    }

    private fun setAdapter() {
        b.rvCups.adapter = AddCupAdapter(cups, object : CupEditListener {
            override fun onEdit(cup: Cup, edit: Boolean) {
                if (edit) {
                    requireActivity().addCupDialog(cup, object : CupClickListener {
                        override fun onClick(item: Cup) {

                            baseActivity.startLoading()
                            makeApiRequest {
                                viewModel.updateCup(item)
                            }
                        }
                    }).show()
                } else {

                    baseActivity.startLoading()
                    makeApiRequest {
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

                    baseActivity.startLoading()
                    makeApiRequest {
                        viewModel.addCup(item, getToken())
                    }
                }
            }).show()
        })
        viewModel.ldCups.observe(viewLifecycleOwner) {
            (requireActivity() as BaseActivity).reloadCups(it)
            cups = it
            b.bAdd.isEnabled = true
            setAdapter()
            baseActivity.stopLoading()
        }
        viewModel.ldAddCup.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldRemoveCup.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getCups(getToken())
        }
    }
}