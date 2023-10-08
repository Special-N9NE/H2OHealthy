package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentCompleteProfileBinding


class CompleteProfileFragment : Fragment() {

    private lateinit var b: FragmentCompleteProfileBinding
    private val genders = arrayListOf("Male", "Female")
    private val activityLevels = arrayListOf("Never", "Low", "Sometimes", "High", "Athlete")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentCompleteProfileBinding.inflate(inflater)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupSpinners()
        setupClicks()

    }

    private fun setupSpinners() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.view_drop_down, activityLevels
        )
        b.spActivity.setAdapter(adapter)

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.view_drop_down, genders
        )
        b.spGender.setAdapter(adapterGender)
    }

    private fun setupClicks() {
        b.etBirthday.setOnClickListener {
            //TODO open date dialog
        }
        b.bNext.setOnClickListener {
            //TODO validation
        }
    }

}