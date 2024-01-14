package org.n9ne.auth.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

@AndroidEntryPoint
class RegisterFragment : BaseFragment<AuthRepo>() {

    private lateinit var b: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

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
        b.viewModel = viewModel

        initRepos(viewModel)
    }

    private fun setClicks() {
        b.bRegister.setOnClickListener {
            b.bRegister.isEnabled = false

            name = b.etName.text.toString()
            email = b.etEmail.text.toString()
            password = b.etPassword.text.toString()

            baseActivity.startLoading()
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

    private fun setupObserver() {
        viewModel.ldRegister.observe(viewLifecycleOwner, EventObserver(listOf(b.bRegister)) {
            val data = Bundle().apply {
                putString("name", name)
                putString("email", email)
                putString("password", password)
            }
            this.shouldNavigate(R.id.register_to_completeProfile, data)

            baseActivity.stopLoading()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver(listOf(b.bRegister)) {
            baseActivity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}