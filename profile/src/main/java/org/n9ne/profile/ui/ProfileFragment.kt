package org.n9ne.profile.ui

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.common.BaseFragment
import org.n9ne.common.R.color
import org.n9ne.common.R.string
import org.n9ne.common.dialog.createLeagueDialog
import org.n9ne.common.dialog.joinLeagueDialog
import org.n9ne.common.dialog.reminderDialog
import org.n9ne.common.model.Setting
import org.n9ne.common.model.SettingItem
import org.n9ne.common.model.User
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.Saver.isAppEnglish
import org.n9ne.common.util.Saver.saveToken
import org.n9ne.common.util.Saver.setLanguage
import org.n9ne.common.util.interfaces.AddLeagueListener
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.common.util.interfaces.ReminderSaveListener
import org.n9ne.common.util.interfaces.SettingClickListener
import org.n9ne.common.util.reminderNotification
import org.n9ne.common.util.scheduleNotification
import org.n9ne.common.util.setGradient
import org.n9ne.common.util.setUserAvatar
import org.n9ne.profile.R
import org.n9ne.profile.databinding.FragmentProfileBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.ui.adpter.SettingAdapter
import org.n9ne.profile.ui.viewModel.ProfileViewModel

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileRepo, FragmentProfileBinding>(), RefreshListener {

    private val viewModel: ProfileViewModel by viewModels()
    private var createLeagueDialog: Dialog? = null
    private var joinLeagueDialog: Dialog? = null

    private var hasLeague: Boolean = false

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showNavigation()

        createFragment()

        makeLocalRequest {
            viewModel.getUser(getToken())
        }
    }

    override fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)

        b.rvSettings.adapter =
            SettingAdapter(viewModel.getSettings(), object : SettingClickListener {
                override fun settingClicked(setting: Setting) {
                    when (setting.type) {
                        //TODO
                        SettingItem.PASSWORD -> Log.e("WWW", "")
                        SettingItem.STATS -> shouldNavigate(R.id.profile_to_stats)
                        SettingItem.GLASS -> shouldNavigate(R.id.profile_to_cups)
                        SettingItem.LANGUAGE -> {
                            setLanguage(!isAppEnglish())
                            baseActivity.reloadLanguage()
                        }

                        SettingItem.REMINDER -> requireActivity().reminderDialog(object :
                            ReminderSaveListener {
                            override fun onSave(hours: Int) {
                                Saver.saveReminder(hours)
                                Toast.makeText(requireContext(), string.saved, Toast.LENGTH_LONG)
                                    .show()

                                requireContext().reminderNotification(hours != 0)
                                requireContext().scheduleNotification(hours)
                            }
                        }).show()
                    }
                }

            })

        setGradients()
    }

    override fun setClicks() {
        b.cvLeague.setOnClickListener {
            if (hasLeague) {
                shouldNavigate(R.id.profile_to_league)
            } else {
                initDialogs()
            }
        }
        b.clLogout.setOnClickListener {
            startLoading()
            viewModel.logout()
        }
    }

    override fun setObservers() {
        viewModel.ldUser.observe(viewLifecycleOwner) {
            setUser(it)
            stopLoading()
        }
        viewModel.ldLogout.observe(viewLifecycleOwner, EventObserver {
            saveToken(null)
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("h2ohealthy://auth")
            )
            requireActivity().startActivity(intent)
            requireActivity().finish()
        })
        viewModel.ldContactClick.observe(viewLifecycleOwner, EventObserver {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, it)
            }

            try {
                startActivity(
                    Intent.createChooser(
                        intent,
                        string.emailClient.toString()
                    )
                )
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(), string.errorEmailClient.toString(), Toast.LENGTH_SHORT
                ).show()
            }

        })
        viewModel.ldToken.observe(viewLifecycleOwner, EventObserver {
            saveToken(null)
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("h2ohealthy://auth")
            )
            requireActivity().startActivity(intent)
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

            stopLoading()

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
            stopLoading()
        })
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

    private fun setGradients() {
        b.tvAge.setGradient(requireContext(), color.linearBlueStart, color.linearBlueEnd)
        b.tvWeight.setGradient(requireContext(), color.linearBlueStart, color.linearBlueEnd)
        b.tvHeight.setGradient(requireContext(), color.linearBlueStart, color.linearBlueEnd)
        b.tvLeague.setGradient(requireContext(), color.linearBlueStart, color.linearBlueEnd)

        b.tvScore.setGradient(requireContext(), color.linearPurpleStart, color.linearPurpleEnd)
    }

    private fun setUser(user: User) {
        hasLeague = user.idLeague != 0L
        b.tvName.text = user.name
        b.tvScore.text = user.score.toString()
        b.tvWeight.text = "${user.weight}${getString(string.kg)}"
        b.tvHeight.text = "${user.height}${getString(string.cm)}"

        b.tvAge.text = user.age

        b.ivProfile.setUserAvatar(user.profile)
    }

    private fun initDialogs() {
        val joinClick = OnClickListener {
            joinLeagueDialog = requireActivity().joinLeagueDialog(object : AddLeagueListener {
                override fun addLeague(input: String) {
                    startLoading()
                    setEnableDialog(joinLeagueDialog!!, false)
                    makeApiRequest {
                        viewModel.joinLeague(input, getToken())
                    }
                }
            })
            joinLeagueDialog!!.show()
        }
        val createClick = object : AddLeagueListener {
            override fun addLeague(input: String) {
                makeApiRequest {
                    viewModel.createLeague(input, getToken(), requireContext())
                }
                createLeagueDialog!!.dismiss()
            }
        }
        createLeagueDialog = requireActivity().createLeagueDialog(false, joinClick, createClick)

        createLeagueDialog!!.show()
    }

    override fun onRefresh() {
        makeApiRequest {
            viewModel.getUser(getToken())
        }
    }
}