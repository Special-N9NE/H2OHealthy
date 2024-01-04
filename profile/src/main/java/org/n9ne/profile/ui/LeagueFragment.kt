package org.n9ne.profile.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.common.BaseActivity
import org.n9ne.common.BaseFragment
import org.n9ne.common.R.string
import org.n9ne.common.dialog.createLeagueDialog
import org.n9ne.common.dialog.leagueSettingDialog
import org.n9ne.common.model.League
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.network.Client
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.getEmail
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.Utils
import org.n9ne.common.util.interfaces.AddLeagueListener
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.profile.databinding.FragmentLeagueBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.repo.ProfileRepoApiImpl
import org.n9ne.profile.repo.ProfileRepoLocalImpl
import org.n9ne.profile.ui.adpter.MemberAdapter
import org.n9ne.profile.ui.viewModel.LeagueViewModel

class LeagueFragment : BaseFragment<ProfileRepo>(), RefreshListener {

    private lateinit var b: FragmentLeagueBinding
    private lateinit var viewModel: LeagueViewModel

    private lateinit var activity: BaseActivity

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

        activity.startLoading()
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
        activity = requireActivity() as BaseActivity
        val client = Client.getInstance()

        apiRepo = ProfileRepoApiImpl(client)
        localRepo = ProfileRepoLocalImpl(AppDatabase.getDatabase(requireContext()).roomDao())

        viewModel = ViewModelProvider(this)[LeagueViewModel::class.java]
        viewModel.db = AppDatabase.getDatabase(requireContext())
        b.viewModel = viewModel

        initRepos(apiRepo as ProfileRepo, localRepo as ProfileRepo, viewModel)

    }

    private fun openSettingDialog(isAdmin: Boolean) {

        val renameClick = if (isAdmin) {
            View.OnClickListener {
                dialogRename =
                    requireActivity().createLeagueDialog(true, null, object : AddLeagueListener {
                        override fun addLeague(input: String) {
                            activity.startLoading()
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
            activity.startLoading()
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
            viewModel.getMembers(getToken())
        }
    }
}