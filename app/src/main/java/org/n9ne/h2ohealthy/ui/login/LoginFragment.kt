package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var b: FragmentLoginBinding
    private var passwordIsVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentLoginBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClicks()

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
            //TODO go to Register
        }
    }

    private fun togglePasswordVisibility() {
        passwordIsVisible = !passwordIsVisible
        if (passwordIsVisible) {

            b.ivPassword.setImageResource(R.drawable.ic_show_password)
            b.etPassword.transformationMethod = null

        } else {
            b.ivPassword.setImageResource(R.drawable.ic_hide)
            b.etPassword.transformationMethod = PasswordTransformationMethod()
        }

        b.etPassword.setSelection(b.etPassword.text.toString().length)
    }

}