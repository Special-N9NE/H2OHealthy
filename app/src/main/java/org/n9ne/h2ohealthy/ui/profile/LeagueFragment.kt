package org.n9ne.h2ohealthy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.databinding.FragmentLeagueBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.dialog.leagueSettingDialog
import org.n9ne.h2ohealthy.ui.profile.adpter.MemberAdapter
import org.n9ne.h2ohealthy.ui.profile.viewModel.LeagueViewModel


class LeagueFragment : Fragment() {

    private lateinit var b: FragmentLeagueBinding
    private lateinit var viewModel: LeagueViewModel

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

        setupObserver()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[LeagueViewModel::class.java]
        b.viewModel = viewModel

        b.rvMember.adapter = MemberAdapter(viewModel.getMembers())

    }

    private fun openSettingDialog(isAdmin: Boolean) {

        val renameClick = if (isAdmin) {
            View.OnClickListener {
                //TODO open rename dialog
            }
        } else null
        val shareClick = View.OnClickListener {
            //TODO share code and name
        }
        val leaveClick = View.OnClickListener {
            //TODO leave league
        }

        val dialog = requireActivity().leagueSettingDialog(
            "My League",
            "code232m",
            renameClick,
            shareClick,
            leaveClick
        )

        dialog.show()
    }

    private fun setupObserver() {
        viewModel.ldSettingClick.observe(viewLifecycleOwner) {
            if (it.notHandled)
                openSettingDialog(it.response)
        }
    }
}