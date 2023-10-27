package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentSplashBinding
import org.n9ne.h2ohealthy.util.setGradient


class SplashFragment : Fragment() {

    private lateinit var b: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSplashBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextColors()
        setupClicks()
    }

    private fun setTextColors() {
        b.tvTitle1.setGradient(requireContext(), R.color.linearBlueStart, R.color.linearBlueEnd)
        b.tvTitle2.setGradient(requireContext(), R.color.linearPurpleStart, R.color.linearPurpleEnd)
    }

    private fun setupClicks() {
        b.bStart.setOnClickListener {
            findNavController().navigate(R.id.splash_to_register)
        }
    }
}