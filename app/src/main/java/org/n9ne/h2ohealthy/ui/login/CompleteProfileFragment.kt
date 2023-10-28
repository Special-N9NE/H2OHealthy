package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentCompleteProfileBinding
import org.n9ne.h2ohealthy.ui.login.viewModel.CompleteProfileViewModel
import org.n9ne.h2ohealthy.util.interfaces.Navigator


class CompleteProfileFragment : Fragment(), Navigator {

    private lateinit var b: FragmentCompleteProfileBinding
    private lateinit var viewModel: CompleteProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentCompleteProfileBinding.inflate(inflater)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        setupSpinners()

    }

    private fun init() {
        viewModel = ViewModelProvider(this)[CompleteProfileViewModel::class.java]
        viewModel.navigator = this
        b.viewModel = viewModel
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

    override fun shouldNavigate(destination: Int) {
        findNavController().navigate(destination)
    }

}