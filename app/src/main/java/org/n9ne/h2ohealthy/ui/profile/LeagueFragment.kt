package org.n9ne.h2ohealthy.ui.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.League
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoApiImpl
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoLocalImpl
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.FragmentLeagueBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.dialog.createLeagueDialog
import org.n9ne.h2ohealthy.ui.dialog.leagueSettingDialog
import org.n9ne.h2ohealthy.ui.profile.adpter.MemberAdapter
import org.n9ne.h2ohealthy.ui.profile.viewModel.LeagueViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Saver.getEmail
import org.n9ne.h2ohealthy.util.Saver.getToken
import org.n9ne.h2ohealthy.util.Utils.isOnline
import org.n9ne.h2ohealthy.util.interfaces.AddLeagueListener
import org.n9ne.h2ohealthy.util.interfaces.RefreshListener


class LeagueFragment : Fragment(), RefreshListener {

    private lateinit var localRepo: ProfileRepoLocalImpl
    private lateinit var apiRepo: ProfileRepoApiImpl

    private lateinit var b: FragmentLeagueBinding
    private lateinit var viewModel: LeagueViewModel

    private lateinit var activity: MainActivity

    private var league: League? = null
    private lateinit var dialogRename: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentLeagueBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).showNavigation()
        init()

        setClicks()
        setupObserver()

        activity.startLoading()
        makeApiRequest {
            viewModel.getMembers(requireActivity().getToken())
        }
    }

    private fun setClicks() {
        b.cvSettings.setOnClickListener {
            league?.let {
                val isAdmin = it.adminEmail!!.trim() == requireActivity().getEmail()!!.trim()
                openSettingDialog(isAdmin)
            }
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

        viewModel = ViewModelProvider(this)[LeagueViewModel::class.java]
        viewModel.db = AppDatabase.getDatabase(requireContext())
        b.viewModel = viewModel

    }

    private fun openSettingDialog(isAdmin: Boolean) {

        val renameClick = if (isAdmin) {
            View.OnClickListener {
                dialogRename =
                    requireActivity().createLeagueDialog(true, null, object : AddLeagueListener {
                        override fun addLeague(input: String) {
                            activity.startLoading()
                            makeApiRequest {
                                viewModel.renameLeague(input, league!!.code)
                            }
                            dialogRename.dismiss()
                        }
                    })

                dialogRename.show()
            }
        } else null
        val shareClick = View.OnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hi there. Use '${league!!.code}' code to join ${league!!.name} League in H2O Healthy Application."
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share))
            requireActivity().startActivity(shareIntent)
        }
        val leaveClick = View.OnClickListener {
            activity.startLoading()
            makeApiRequest {
                viewModel.leave(requireActivity().getToken())
            }
        }

        league?.let {
            val dialog = requireActivity().leagueSettingDialog(
                it.name,
                it.code,
                renameClick,
                shareClick,
                leaveClick
            )

            dialog.show()
        }
    }

    private fun setupObserver() {
        viewModel.ldMembers.observe(viewLifecycleOwner, EventObserver {
            b.rvMember.adapter = MemberAdapter(it)
        })
        viewModel.ldLeague.observe(viewLifecycleOwner, EventObserver {
            league = it
            b.league = it
            activity.stopLoading()
        })
        viewModel.ldLeave.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigateUp()
            activity.stopLoading()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getMembers(requireActivity().getToken())
        }
    }
}