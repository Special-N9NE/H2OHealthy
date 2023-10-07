package org.n9ne.h2ohealthy.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.fragment.app.Fragment
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var b: FragmentRegisterBinding
    private var passwordIsVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentRegisterBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClicks()

        setCheckboxColor()
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

    private fun setupClicks() {
        b.bRegister.setOnClickListener {
            //TODO register
        }
        b.ivGoogle.setOnClickListener {
            //TODO register with google
        }
        b.llLogin.setOnClickListener {
            //TODO go to login
        }
        b.ivPassword.setOnClickListener {
            togglePasswordVisibility()
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