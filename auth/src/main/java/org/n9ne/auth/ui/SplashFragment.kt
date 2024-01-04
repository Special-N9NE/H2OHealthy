package org.n9ne.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.auth.R
import org.n9ne.auth.databinding.FragmentSplashBinding
import org.n9ne.auth.ui.viewModel.SplashViewModel
import org.n9ne.common.BaseActivity
import org.n9ne.common.R.color
import org.n9ne.common.util.Saver.isAppEnglish
import org.n9ne.common.util.Saver.isFirstTime
import org.n9ne.common.util.Saver.setLanguage
import org.n9ne.common.util.interfaces.Navigator
import org.n9ne.common.util.setGradient


class SplashFragment : Fragment(), Navigator {

    private lateinit var b: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel
    private lateinit var activity: BaseActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSplashBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setTextColors()
        setClicks()

        (requireActivity() as BaseActivity).stopLoading()
    }

    private fun setClicks() {
        b.bStart.setOnClickListener {
            if (!isFirstTime()) {
                this.shouldNavigate(R.id.splash_to_login)
            } else
                this.shouldNavigate(R.id.splash_to_register)
        }
        b.ivLanguage.setOnClickListener {
            setLanguage(!isAppEnglish())
            activity.reloadLanguage()
        }
    }

    private fun init() {
        activity = requireActivity() as BaseActivity

        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        viewModel.navigator = this
        b.viewModel = viewModel
    }

    private fun setTextColors() {
        b.tvTitle1.setGradient(requireContext(), color.linearBlueStart, color.linearBlueEnd)
        b.tvTitle2.setGradient(requireContext(), color.linearPurpleStart, color.linearPurpleEnd)
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}