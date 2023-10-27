package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentLoginBinding
import org.n9ne.h2ohealthy.ui.login.viewModel.LoginViewModel


class LoginFragment : Fragment() {

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
        setupClicks()

    }

    private fun init() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun setupClicks() {
        b.ivPassword.setOnClickListener {
            togglePasswordVisibility()
        }
        b.tvForgot.setOnClickListener {
            //TODO send recovery email or something
        }
        b.bLogin.setOnClickListener {
            //TODO process login
        }
        b.ivGoogle.setOnClickListener {
            //TODO login with google
        }
        b.llRegister.setOnClickListener {
            findNavController().navigate(R.id.login_to_register)
        }
    }

    private fun togglePasswordVisibility() {
        viewModel.passwordIsVisible = !viewModel.passwordIsVisible
        if (viewModel.passwordIsVisible) {
            b.ivPassword.setImageResource(R.drawable.ic_show_password)
            b.etPassword.transformationMethod = null

        } else {
            b.ivPassword.setImageResource(R.drawable.ic_hide)
            b.etPassword.transformationMethod = PasswordTransformationMethod()
        }

        b.etPassword.setSelection(b.etPassword.text.toString().length)
    }

}