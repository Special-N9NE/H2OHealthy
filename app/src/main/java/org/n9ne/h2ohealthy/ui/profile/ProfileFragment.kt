package org.n9ne.h2ohealthy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.databinding.FragmentProfileBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.profile.adpter.SettingAdapter
import org.n9ne.h2ohealthy.ui.profile.viewModel.ProfileViewModel
import org.n9ne.h2ohealthy.util.interfaces.Navigator
import org.n9ne.h2ohealthy.util.setGradient


class ProfileFragment : Fragment(), Navigator {

    private lateinit var b: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentProfileBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).showNavigation()
        init()


        setUser(viewModel.user)
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.navigator = this
        b.viewModel = viewModel
        b.rvSettings.adapter = SettingAdapter(viewModel.settings)

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

        //TODO calculate age
        b.tvAge.text = "21"
    }

    override fun shouldNavigate(destination: Int) {
        findNavController().navigate(destination)
    }
}