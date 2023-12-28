package org.n9ne.auth.ui

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.auth.R
import org.n9ne.auth.databinding.FragmentLoginBinding
import org.n9ne.auth.repo.AuthRepoImpl
import org.n9ne.auth.ui.viewModel.LoginViewModel
import org.n9ne.common.BaseActivity
import org.n9ne.common.R.drawable
import org.n9ne.common.source.network.Client
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.saveEmail
import org.n9ne.common.util.Saver.saveToken
import org.n9ne.common.util.interfaces.Navigator


class LoginFragment : Fragment(), Navigator {

    private lateinit var b: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var activity: BaseActivity

    private lateinit var email: String

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
        activity = requireActivity() as BaseActivity
        val client = Client.getInstance()
        val repo = AuthRepoImpl(client)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.navigator = this
        viewModel.repo = repo
        b.viewModel = viewModel
    }

    private fun setClicks() {
        b.bLogin.setOnClickListener {
            b.bLogin.isEnabled = true

            email = b.etEmail.text.toString().trim()
            activity.startLoading()
            viewModel.login(
                email,
                b.etPassword.text.toString(),
                requireContext()
            )
        }
    }

    private fun setupObserver() {
        viewModel.ldPasswordClick.observe(viewLifecycleOwner) {
            if (it) {
                b.ivPassword.setImageResource(drawable.ic_show_password)
                b.etPassword.transformationMethod = null

            } else {
                b.ivPassword.setImageResource(drawable.ic_hide)
                b.etPassword.transformationMethod = PasswordTransformationMethod()
            }
            b.etPassword.setSelection(b.etPassword.text.toString().length)
        }
        viewModel.ldUserToken.observe(viewLifecycleOwner, EventObserver(listOf(b.bLogin)) {
            requireActivity().saveEmail(email)
            requireActivity().saveToken(it)
            activity.stopLoading()
        })
        viewModel.ldName.observe(viewLifecycleOwner, EventObserver(listOf(b.bLogin)) {
            val data = Bundle().apply {
                putString("name", it)
            }
            this.shouldNavigate(R.id.login_to_loginDone, data)
        })
        viewModel.ldError.observe(viewLifecycleOwner,
            EventObserver(listOf(b.bLogin)) {
                activity.stopLoading()
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }

}