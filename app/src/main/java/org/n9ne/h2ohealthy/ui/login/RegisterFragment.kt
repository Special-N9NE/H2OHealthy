package org.n9ne.h2ohealthy.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.auth.AuthRepoImpl
import org.n9ne.h2ohealthy.databinding.FragmentRegisterBinding
import org.n9ne.h2ohealthy.ui.login.viewModel.RegisterViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.interfaces.Navigator


class RegisterFragment : Fragment(), Navigator {

    private lateinit var b: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentRegisterBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        setCheckboxColor()
        setupObserver()

        setClicks()
    }


    private fun init() {
        val client = (requireActivity().application as App).client
        val repo = AuthRepoImpl(client)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        viewModel.repo = repo
        viewModel.navigator = this

        b.viewModel = viewModel
    }

    private fun setClicks() {
        b.bRegister.setOnClickListener {
            b.bRegister.isEnabled = false

            name = b.etName.text.toString()
            email = b.etEmail.text.toString()
            password = b.etPassword.text.toString()

            viewModel.register(name, email, password)
        }
    }

    private fun setCheckboxColor() {
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                ResourcesCompat.getColor(resources, R.color.gray, requireContext().theme),
                ResourcesCompat.getColor(resources, R.color.linearBlueEnd, requireContext().theme)
            )
        )
        CompoundButtonCompat.setButtonTintList(b.cbPolicy, colorStateList)
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

        viewModel.ldRegister.observe(viewLifecycleOwner, EventObserver(b.bRegister) {
            val data = Bundle().apply {
                putString("name", name)
                putString("email", email)
                putString("password", password)
            }
            this.shouldNavigate(R.id.register_to_completeProfile, data)
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver(b.bRegister) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}