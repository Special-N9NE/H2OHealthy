package org.n9ne.profile.ui

import android.os.Bundle
import android.view.View
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
class CupsFragment : BaseFragment<ProfileRepo,FragmentCupsBinding>(), RefreshListener {
    private var cups = listOf<Cup>()
    private val viewModel: CupsViewModel by viewModels()


    override fun getViewBinding(): FragmentCupsBinding =
        FragmentCupsBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideNavigation()

        createFragment()

        makeRequest {
            viewModel.getCups(getToken())
        }
    }

    override fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)
    }

    override fun setClicks() {}

    private fun setAdapter() {
        b.rvCups.adapter = AddCupAdapter(cups, object : CupEditListener {
            override fun onEdit(cup: Cup, edit: Boolean) {
                if (edit) {
                    requireActivity().addCupDialog(cup, object : CupClickListener {
                        override fun onClick(item: Cup) {

                            makeApiRequest {
                                viewModel.updateCup(item)
                            }
                        }
                    }).show()
                } else {

                    makeApiRequest {
                        viewModel.removeCup(cup)
                    }
                }
            }
        })
    }

    override fun setObservers() {
        viewModel.ldShowDialog.observe(viewLifecycleOwner, EventObserver {
            requireActivity().addCupDialog(null, object : CupClickListener {
                override fun onClick(item: Cup) {

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
            stopLoading()
        }
        viewModel.ldAddCup.observe(viewLifecycleOwner, EventObserver {
            stopLoading()
            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldRemoveCup.observe(viewLifecycleOwner, EventObserver {
            stopLoading()
            Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getCups(getToken())
        }
    }
}