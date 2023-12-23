package org.n9ne.h2ohealthy.ui.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Setting
import org.n9ne.h2ohealthy.data.model.SettingItem
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoApiImpl
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoLocalImpl
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.FragmentProfileBinding
import org.n9ne.h2ohealthy.ui.AuthActivity
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.dialog.createLeagueDialog
import org.n9ne.h2ohealthy.ui.dialog.joinLeagueDialog
import org.n9ne.h2ohealthy.ui.profile.adpter.SettingAdapter
import org.n9ne.h2ohealthy.ui.profile.viewModel.ProfileViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Saver.getToken
import org.n9ne.h2ohealthy.util.Saver.saveToken
import org.n9ne.h2ohealthy.util.Utils.isOnline
import org.n9ne.h2ohealthy.util.interfaces.AddLeagueListener
import org.n9ne.h2ohealthy.util.interfaces.Navigator
import org.n9ne.h2ohealthy.util.interfaces.RefreshListener
import org.n9ne.h2ohealthy.util.interfaces.SettingClickListener
import org.n9ne.h2ohealthy.util.setGradient


class ProfileFragment : Fragment(), Navigator, RefreshListener {

    private lateinit var localRepo: ProfileRepoLocalImpl
    private lateinit var apiRepo: ProfileRepoApiImpl

    private lateinit var b: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var createLeagueDialog: Dialog? = null
    private var joinLeagueDialog: Dialog? = null
    private lateinit var activity: MainActivity
    private var hasLeague: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentProfileBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).showNavigation()
        init()
        setupObserver()
        setClicks()

        makeLocalRequest {
            viewModel.getUser(requireActivity().getToken())
        }
    }

    fun setEnableDialog(dialog: Dialog, enable: Boolean) {
        val viewGroup = dialog.window?.decorView?.findViewById<ViewGroup>(android.R.id.content)
        viewGroup?.let { disableEnableControls(enable, it) }
    }

    private fun disableEnableControls(enable: Boolean, viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            child.isEnabled = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
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

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.db = AppDatabase.getDatabase(requireContext())
        viewModel.navigator = this
        b.viewModel = viewModel

        b.rvSettings.adapter = SettingAdapter(viewModel.settings, object : SettingClickListener {
            override fun settingClicked(setting: Setting) {
                when (setting.type) {
                    //TODO
                    SettingItem.PASSWORD -> Log.e("WWW", "")
                    SettingItem.STATS -> Log.e("WWW", "")
                    SettingItem.GLASS -> shouldNavigate(R.id.profile_to_cups)
                }
            }

        })

        setGradients()
    }

    private fun setGradients() {
        b.tvAge.setGradient(requireContext(), R.color.linearBlueStart, R.color.linearBlueEnd)
        b.tvWeight.setGradient(requireContext(), R.color.linearBlueStart, R.color.linearBlueEnd)
        b.tvHeight.setGradient(requireContext(), R.color.linearBlueStart, R.color.linearBlueEnd)
        b.tvLeague.setGradient(requireContext(), R.color.linearBlueStart, R.color.linearBlueEnd)

        b.tvScore.setGradient(requireContext(), R.color.linearPurpleStart, R.color.linearPurpleEnd)
    }

    private fun setClicks() {
        b.cvLeague.setOnClickListener {
            if (hasLeague) {
                shouldNavigate(R.id.profile_to_league)
            } else {
                initDialogs()
            }
        }
        b.clLogout.setOnClickListener {
            activity.startLoading()
            viewModel.logout()
        }
    }

    private fun setUser(user: User) {
        hasLeague = user.idLeague != 0L
        b.tvName.text = user.name
        b.tvScore.text = user.score.toString()
        b.tvWeight.text = "${user.weight}${getString(R.string.kg)}"
        b.tvHeight.text = "${user.height}${getString(R.string.cm)}"

        b.tvAge.text = user.age

        if (user.profile.isNotEmpty()) {
            Glide.with(requireContext()).load(user.profile)
                .apply(
                    RequestOptions().placeholder(R.drawable.image_profile)
                        .error(R.drawable.image_profile)
                ).into(b.ivProfile)
        }
    }

    private fun setupObserver() {
        viewModel.ldUser.observe(viewLifecycleOwner) {
            setUser(it)
            activity.stopLoading()
        }
        viewModel.ldLogout.observe(viewLifecycleOwner, EventObserver {
            requireActivity().saveToken(null)
            requireActivity().startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        })
        viewModel.ldContactClick.observe(viewLifecycleOwner, EventObserver {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, it)
            }

            try {
                startActivity(Intent.createChooser(intent, "Choose an Email client :"))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(), "There are no email clients installed.", Toast.LENGTH_SHORT
                ).show()
            }

        })
        viewModel.ldToken.observe(viewLifecycleOwner, EventObserver {
            requireActivity().saveToken(null)
            requireActivity().startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        })
        viewModel.ldJoinLeague.observe(viewLifecycleOwner, EventObserver {
            joinLeagueDialog?.let {
                setEnableDialog(it, true)
            }
            createLeagueDialog?.let {
                setEnableDialog(it, true)
            }

            hasLeague = true

            createLeagueDialog?.dismiss()
            joinLeagueDialog?.dismiss()

            activity.stopLoading()

            shouldNavigate(R.id.profile_to_league)
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {

            joinLeagueDialog?.let { d ->
                setEnableDialog(d, true)
            }
            createLeagueDialog?.let { d ->
                setEnableDialog(d, true)
            }

            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            activity.stopLoading()
        })
    }

    private fun initDialogs() {
        val joinClick = OnClickListener {
            joinLeagueDialog = requireActivity().joinLeagueDialog(object : AddLeagueListener {
                override fun addLeague(input: String) {
                    activity.startLoading()
                    setEnableDialog(joinLeagueDialog!!, false)
                    makeApiRequest {
                        viewModel.joinLeague(input, requireActivity().getToken())
                    }
                }
            })
            joinLeagueDialog!!.show()
        }
        val createClick = object : AddLeagueListener {
            override fun addLeague(input: String) {
                makeApiRequest {
                    viewModel.createLeague(input, requireActivity().getToken())
                }
                createLeagueDialog!!.dismiss()
            }
        }
        createLeagueDialog = requireActivity().createLeagueDialog(false, joinClick, createClick)

        createLeagueDialog!!.show()
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getUser(requireActivity().getToken())
        }
    }
}