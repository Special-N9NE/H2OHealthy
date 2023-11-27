package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentSplashBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.login.viewModel.SplashViewModel
import org.n9ne.h2ohealthy.util.Saver.isFirstTime
import org.n9ne.h2ohealthy.util.Saver.setFirstTime
import org.n9ne.h2ohealthy.util.interfaces.Navigator
import org.n9ne.h2ohealthy.util.setGradient


class SplashFragment : Fragment(), Navigator {

    private lateinit var b: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSplashBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).hideNavigation()
        init()
        setTextColors()

        if (requireActivity().isFirstTime()) {
            viewModel.initDatabase(requireContext())
            requireActivity().setFirstTime(false)
        }
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        viewModel.navigator = this
        b.viewModel = viewModel
    }

    private fun setTextColors() {
        b.tvTitle1.setGradient(requireContext(), R.color.linearBlueStart, R.color.linearBlueEnd)
        b.tvTitle2.setGradient(requireContext(), R.color.linearPurpleStart, R.color.linearPurpleEnd)
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}