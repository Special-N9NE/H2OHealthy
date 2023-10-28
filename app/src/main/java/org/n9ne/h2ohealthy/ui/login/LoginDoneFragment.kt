package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.databinding.FragmentLoginDoneBinding
import org.n9ne.h2ohealthy.ui.login.viewModel.LoginDoneViewModel
import org.n9ne.h2ohealthy.util.interfaces.Navigator


class LoginDoneFragment : Fragment(), Navigator {

    private lateinit var b: FragmentLoginDoneBinding
    private lateinit var viewModel: LoginDoneViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentLoginDoneBinding.inflate(inflater)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[LoginDoneViewModel::class.java]
        viewModel.navigator = this
        b.viewModel = viewModel
    }

    override fun shouldNavigate(destination: Int) {
        findNavController().navigate(destination)
    }
}