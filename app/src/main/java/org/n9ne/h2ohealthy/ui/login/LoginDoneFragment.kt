package org.n9ne.h2ohealthy.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.n9ne.h2ohealthy.databinding.FragmentLoginDoneBinding


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


        b.bGo.setOnClickListener {
            //TODO validation
        }

    }
}