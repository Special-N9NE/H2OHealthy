package org.n9ne.auth.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.auth.databinding.FragmentLoginDoneBinding
import org.n9ne.auth.repo.AuthRepo
import org.n9ne.common.BaseFragment
import org.n9ne.common.util.Saver.setFirstTime


@AndroidEntryPoint
class LoginDoneFragment : BaseFragment<AuthRepo, FragmentLoginDoneBinding>() {

    override fun getViewBinding(): FragmentLoginDoneBinding =
        FragmentLoginDoneBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = requireArguments().getString("name")
        b.tvName.text = name

        createFragment()

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
    override fun init() {}

    override fun setClicks() {}

    override fun setObservers() {}
}