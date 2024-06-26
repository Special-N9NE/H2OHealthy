package org.n9ne.auth.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.auth.R
import org.n9ne.auth.databinding.FragmentRegisterBinding
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.auth.ui.viewModel.RegisterViewModel
import org.n9ne.common.BaseFragment
import org.n9ne.common.R.color
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.errorToast

@AndroidEntryPoint
class RegisterFragment : BaseFragment<AuthRepo, FragmentRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun getViewBinding(): FragmentRegisterBinding =
        FragmentRegisterBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createFragment()

        setCheckboxColor()
    }


    override fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)
    }

    override fun setClicks() {
        b.bRegister.setOnClickListener {
            b.bRegister.isEnabled = false

            name = b.etName.text.toString()
            email = b.etEmail.text.toString()
            password = b.etPassword.text.toString()

            makeApiRequest {
                viewModel.register(name, email, password, requireContext())
            }
        }
    }

    private fun setCheckboxColor() {
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                ResourcesCompat.getColor(resources, color.gray, requireContext().theme),
                ResourcesCompat.getColor(resources, color.linearBlueEnd, requireContext().theme)
            )
        )
        CompoundButtonCompat.setButtonTintList(b.cbPolicy, colorStateList)
    }

    override fun setObservers() {
        viewModel.ldRegister.observe(viewLifecycleOwner, EventObserver(listOf(b.bRegister)) {
            val data = Bundle().apply {
                putString("name", name)
                putString("email", email)
                putString("password", it)
            }
            this.shouldNavigate(R.id.register_to_completeProfile, data)

            stopLoading()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver(listOf(b.bRegister)) {
            stopLoading()
            requireContext().errorToast(it)
        })

    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}