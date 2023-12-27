package org.n9ne.h2ohealthy.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.n9ne.h2ohealthy.databinding.FragmentLoginDoneBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.common.util.Saver.setFirstTime


class LoginDoneFragment : Fragment() {

    private lateinit var b: FragmentLoginDoneBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentLoginDoneBinding.inflate(inflater)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = requireArguments().getString("name")
        b.tvName.text = name

        b.bGo.setOnClickListener {
            requireActivity().setFirstTime(false)
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}