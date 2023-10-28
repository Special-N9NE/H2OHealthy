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
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.navigator = this
        b.viewModel = viewModel
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
    }

    override fun shouldNavigate(destination: Int) {
        findNavController().navigate(destination)
    }

}