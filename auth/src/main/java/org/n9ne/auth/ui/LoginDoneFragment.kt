package org.n9ne.auth.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.auth.databinding.FragmentLoginDoneBinding
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseFragment
import org.n9ne.common.util.Saver.setFirstTime


@AndroidEntryPoint
class LoginDoneFragment : BaseFragment<AuthRepo>() {

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
            setFirstTime(false)
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("h2ohealthy://home")
            )
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
    }
}