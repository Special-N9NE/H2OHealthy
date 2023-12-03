package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.auth.AuthRepoImpl
import org.n9ne.h2ohealthy.databinding.FragmentLoginBinding
import org.n9ne.h2ohealthy.ui.login.viewModel.LoginViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Saver.saveToken
import org.n9ne.h2ohealthy.util.interfaces.Navigator


class LoginFragment : Fragment(), Navigator {

    private lateinit var b: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentLoginBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setupObserver()
        setClicks()
    }


    private fun init() {
        val client = (requireActivity().application as App).client
        val repo = AuthRepoImpl(client)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.navigator = this
        viewModel.repo = repo
        b.viewModel = viewModel
    }

    private fun setClicks() {
        b.bLogin.setOnClickListener {
            b.bLogin.isEnabled = true

            viewModel.login(
                b.etEmail.text.toString(),
                b.etPassword.text.toString(),
                requireContext()
            )
        }
    }

    private fun setupObserver() {
        viewModel.ldPasswordClick.observe(viewLifecycleOwner) {
            if (it) {
                b.ivPassword.setImageResource(R.drawable.ic_show_password)
                b.etPassword.transformationMethod = null

            } else {
                b.ivPassword.setImageResource(R.drawable.ic_hide)
                b.etPassword.transformationMethod = PasswordTransformationMethod()
            }
            b.etPassword.setSelection(b.etPassword.text.toString().length)
        }
        viewModel.ldToken.observe(viewLifecycleOwner, EventObserver(listOf(b.bLogin)) {
            requireActivity().saveToken(it)
            this.shouldNavigate(R.id.login_to_loginDone)
        })
        viewModel.ldError.observe(viewLifecycleOwner,
            EventObserver(listOf(b.bLogin)) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }

}