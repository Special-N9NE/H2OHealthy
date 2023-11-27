package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.AuthRepoImpl
import org.n9ne.h2ohealthy.databinding.FragmentCompleteProfileBinding
import org.n9ne.h2ohealthy.ui.login.viewModel.CompleteProfileViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Saver.saveToken
import org.n9ne.h2ohealthy.util.interfaces.Navigator


class CompleteProfileFragment : Fragment(), Navigator {

    private lateinit var b: FragmentCompleteProfileBinding
    private lateinit var viewModel: CompleteProfileViewModel
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentCompleteProfileBinding.inflate(inflater)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = requireArguments().getString("name")!!
        email = requireArguments().getString("email")!!
        password = requireArguments().getString("password")!!

        init()
        setupSpinners()
        setObservers()

        setClicks()
    }

    private fun init() {
        val client = (requireActivity().application as App).client
        val repo = AuthRepoImpl(client)

        viewModel = ViewModelProvider(this)[CompleteProfileViewModel::class.java]
        viewModel.navigator = this

        viewModel.repo = repo
        b.viewModel = viewModel
    }

    private fun setClicks() {
        b.bNext.setOnClickListener {
            b.bNext.isEnabled = false
            viewModel.completeProfile(
                name, email, password, b.spActivity.text.toString(), b.spGender.text.toString(),
                "2002/11/30", b.etWeight.text.toString(), b.etHeight.text.toString()
            )
        }
    }

    private fun setupSpinners() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.view_drop_down, viewModel.activityLevels
        )
        b.spActivity.setAdapter(adapter)

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.view_drop_down, viewModel.genders
        )
        b.spGender.setAdapter(adapterGender)
    }

    private fun setObservers() {
        viewModel.ldToken.observe(viewLifecycleOwner, EventObserver(b.bNext) {
            requireActivity().saveToken(it)
            this.shouldNavigate(R.id.completeProfile_to_loginDone)
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}