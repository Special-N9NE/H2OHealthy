package org.n9ne.auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.auth.R
import org.n9ne.auth.databinding.FragmentSplashBinding
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.auth.ui.viewModel.SplashViewModel
import org.n9ne.common.BaseFragment
import org.n9ne.common.R.color
import org.n9ne.common.util.Saver.isAppEnglish
import org.n9ne.common.util.Saver.isFirstTime
import org.n9ne.common.util.Saver.setLanguage
import org.n9ne.common.util.setGradient

@AndroidEntryPoint
class SplashFragment : BaseFragment<AuthRepo, FragmentSplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()

    override fun getViewBinding(): FragmentSplashBinding =
        FragmentSplashBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createFragment()

        setTextColors()
    }

    override fun init() {
        initRepos(viewModel)
    }

    override fun setClicks() {
        b.bStart.setOnClickListener {
            if (!isFirstTime()) {
                this.shouldNavigate(R.id.splash_to_login)
            } else
                this.shouldNavigate(R.id.splash_to_register)
        }
        b.ivLanguage.setOnClickListener {
            setLanguage(!isAppEnglish())
            baseActivity.reloadLanguage()
        }
    }

    override fun setObservers() {}


    private fun setTextColors() {
        b.tvTitle1.setGradient(requireContext(), color.linearBlueStart, color.linearBlueEnd)
        b.tvTitle2.setGradient(requireContext(), color.linearPurpleStart, color.linearPurpleEnd)
    }
}