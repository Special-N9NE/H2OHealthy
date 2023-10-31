package org.n9ne.h2ohealthy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentEditProfileBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.profile.viewModel.EditProfileViewModel
import org.n9ne.h2ohealthy.util.interfaces.Navigator


class EditProfileFragment : Fragment(), Navigator {

    private lateinit var b: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentEditProfileBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).showNavigation()
        init()

        setupObserver()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        b.viewModel = viewModel

        setupSpinners()
    }


    private fun setupSpinners() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.view_drop_down, viewModel.activityLevels
        )
        b.spActivity.setAdapter(adapter)

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.view_drop_down, viewModel.genders
        )
        b.spGender.setAdapter(adapterGender)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            b.ivProfile.setImageURI(galleryUri)
            //TODO save image
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupObserver() {
        viewModel.ldPickImage.observe(viewLifecycleOwner) {
            galleryLauncher.launch("image/*")
        }
        viewModel.ldSubmit.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    override fun shouldNavigate(destination: Int) {
        findNavController().navigate(destination)
    }

}