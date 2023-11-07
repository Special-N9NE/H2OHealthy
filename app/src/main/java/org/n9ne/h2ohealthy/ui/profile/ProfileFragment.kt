package org.n9ne.h2ohealthy.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.Setting
import org.n9ne.h2ohealthy.data.model.SettingItem
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.ProfileRepo
import org.n9ne.h2ohealthy.data.repo.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.FragmentProfileBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.dialog.createLeagueDialog
import org.n9ne.h2ohealthy.ui.dialog.joinLeagueDialog
import org.n9ne.h2ohealthy.ui.profile.adpter.SettingAdapter
import org.n9ne.h2ohealthy.ui.profile.viewModel.ProfileViewModel
import org.n9ne.h2ohealthy.ui.profile.viewModel.ProfileViewModelFactory
import org.n9ne.h2ohealthy.util.interfaces.AddLeagueListener
import org.n9ne.h2ohealthy.util.interfaces.Navigator
import org.n9ne.h2ohealthy.util.interfaces.SettingClickListener
import org.n9ne.h2ohealthy.util.setGradient


class ProfileFragment : Fragment(), Navigator {

    private lateinit var b: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var createLeagueDialog: Dialog
    private lateinit var joinLeagueDialog: Dialog


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

        viewModel.getUser()
        setupObserver()
    }

    private fun init() {
        val repo = ProfileRepo(AppDatabase.getDatabase(requireContext()).roomDao())
        viewModel =
            ViewModelProvider(this, ProfileViewModelFactory(repo))[ProfileViewModel::class.java]

        viewModel.navigator = this
        b.viewModel = viewModel
        b.rvSettings.adapter = SettingAdapter(viewModel.settings, object : SettingClickListener {
            override fun settingClicked(setting: Setting) {
                when (setting.type) {
                    SettingItem.PASSWORD -> TODO()
                    SettingItem.HISTORY -> TODO()
                    SettingItem.PROGRESS -> TODO()
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

    private fun setUser(user: User) {
        b.tvName.text = user.name
        b.tvScore.text = user.score.toString()
        b.tvWeight.text = "${user.weight}${getString(R.string.kg)}"
        b.tvHeight.text = "${user.height}${getString(R.string.cm)}"

        b.tvAge.text = user.age

        if (user.profile.isNotEmpty()) {
            Glide.with(requireContext())
                .load(user.profile)
                .into(b.ivProfile)
                .onLoadFailed(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.image_profile
                    )
                )
        }
    }

    private fun setupObserver() {
        viewModel.ldInLeague.observe(viewLifecycleOwner) {
            if (it.notHandled) {
                if (it.response) {
                    shouldNavigate(R.id.profile_to_league)
                } else {
                    openLeagueDialogs()
                }
            }
        }
        viewModel.ldUser.observe(viewLifecycleOwner) {
            setUser(it)
        }
    }

    private fun openLeagueDialogs() {
        val joinClick = OnClickListener {
            joinLeagueDialog = requireActivity().joinLeagueDialog(object : AddLeagueListener {
                override fun addLeague(input: String) {
                    //TODO validation for join
                    createLeagueDialog.dismiss()
                    joinLeagueDialog.dismiss()
                }
            })
            joinLeagueDialog.show()
        }
        val createClick = object : AddLeagueListener {
            override fun addLeague(input: String) {
                //TODO validation
                createLeagueDialog.dismiss()
            }
        }
        createLeagueDialog = requireActivity().createLeagueDialog(joinClick, createClick)

        createLeagueDialog.show()
    }

    override fun shouldNavigate(destination: Int) {
        findNavController().navigate(destination)
    }
}