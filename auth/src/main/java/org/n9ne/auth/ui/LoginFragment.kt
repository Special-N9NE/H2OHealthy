package org.n9ne.auth.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.auth.R
import org.n9ne.auth.databinding.FragmentLoginBinding
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.auth.ui.viewModel.LoginViewModel
import org.n9ne.common.BaseFragment
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.saveEmail
import org.n9ne.common.util.Saver.saveToken
import org.n9ne.common.util.Utils


@AndroidEntryPoint
class LoginFragment : BaseFragment<AuthRepo, FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var email: String


    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createFragment()

        b.etPassword.textDirection =
            if (Utils.isLocalPersian()) View.TEXT_DIRECTION_RTL else View.TEXT_DIRECTION_LTR

    }

    override fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)
    }

    override fun setClicks() {
        b.bLogin.setOnClickListener {
            b.bLogin.isEnabled = true

            email = b.etEmail.text.toString().trim()

            makeApiRequest {
                viewModel.login(
                    email,
                    b.etPassword.text.toString(),
                    requireContext()
                )
            }
        }
    }

    override fun setObservers() {
        viewModel.ldUserToken.observe(viewLifecycleOwner, EventObserver(listOf(b.bLogin)) {
            saveEmail(email)
            saveToken(it)
            stopLoading()
        })
        viewModel.ldName.observe(viewLifecycleOwner, EventObserver(listOf(b.bLogin)) {
            val data = Bundle().apply {
                putString("name", it)
            }
            this.shouldNavigate(R.id.login_to_loginDone, data)
        })
        viewModel.ldError.observe(viewLifecycleOwner,
            EventObserver(listOf(b.bLogin)) {
                stopLoading()
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }

}