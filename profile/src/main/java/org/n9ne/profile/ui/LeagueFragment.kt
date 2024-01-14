package org.n9ne.profile.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.common.BaseActivity
import org.n9ne.common.BaseFragment
import org.n9ne.common.R.string
import org.n9ne.common.dialog.createLeagueDialog
import org.n9ne.common.dialog.leagueSettingDialog
import org.n9ne.common.model.League
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.getEmail
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.Utils
import org.n9ne.common.util.interfaces.AddLeagueListener
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.profile.databinding.FragmentLeagueBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.ui.adpter.MemberAdapter
import org.n9ne.profile.ui.viewModel.LeagueViewModel

@AndroidEntryPoint
class LeagueFragment : BaseFragment<ProfileRepo>(), RefreshListener {

    private lateinit var b: FragmentLeagueBinding
    private val viewModel: LeagueViewModel by viewModels()

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

        (requireActivity() as BaseActivity).showNavigation()
        init()

        setClicks()
        setupObserver()

        baseActivity.startLoading()
        makeApiRequest {
            viewModel.getMembers(getToken())
        }
    }

    private fun setClicks() {
        b.cvSettings.setOnClickListener {
            league?.let {
                val isAdmin = it.adminEmail!!.trim() == getEmail()!!.trim()
                openSettingDialog(isAdmin)
            }
        }
    }

    private fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)
    }

    private fun openSettingDialog(isAdmin: Boolean) {

        val renameClick = if (isAdmin) {
            View.OnClickListener {
                dialogRename =
                    requireActivity().createLeagueDialog(true, null, object : AddLeagueListener {
                        override fun addLeague(input: String) {
                            baseActivity.startLoading()
                            makeApiRequest {
                                viewModel.renameLeague(input, league!!.code, requireContext())
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
                val text = if (Utils.isLocalPersian())
                    "سلام چطوری؟ از کد'${league!!.code}' استفاده کن تا وارد لیگ ${league!!.name} توی اپلیکیشن H2O Healthy بشی."
                else
                    "Hi there. Use '${league!!.code}' code to join ${league!!.name} League in H2O Healthy Application."

                putExtra(
                    Intent.EXTRA_TEXT,
                    text
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, getString(string.share))
            requireActivity().startActivity(shareIntent)
        }
        val leaveClick = View.OnClickListener {
            baseActivity.startLoading()
            makeApiRequest {
                viewModel.leave(getToken())
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
            baseActivity.stopLoading()
        })
        viewModel.ldLeave.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigateUp()
            baseActivity.stopLoading()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getMembers(getToken())
        }
    }
}