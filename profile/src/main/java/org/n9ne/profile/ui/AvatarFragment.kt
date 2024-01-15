package org.n9ne.profile.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.common.BaseFragment
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.interfaces.AvatarClickListener
import org.n9ne.profile.databinding.FragmentAvatarsBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.ui.adpter.AvatarAdapter
import org.n9ne.profile.ui.viewModel.AvatarsViewModel

@AndroidEntryPoint
class AvatarFragment : BaseFragment<ProfileRepo,FragmentAvatarsBinding>() {

    private val viewModel: AvatarsViewModel by viewModels()

    override fun getViewBinding(): FragmentAvatarsBinding =
        FragmentAvatarsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideNavigation()

        createFragment()

        setAdapter()
    }

    override fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)
    }

    override fun setClicks() {}

    private fun setAdapter() {
        b.rv.adapter = AvatarAdapter(viewModel.avatars, object : AvatarClickListener {
            override fun onClick(image: String) {
                startLoading()
                makeApiRequest {
                    viewModel.saveProfile(getToken(), image)
                }
            }
        })
    }

    override fun setObservers() {
        viewModel.ldSuccess.observe(viewLifecycleOwner, EventObserver {
            stopLoading()
            findNavController().navigateUp()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }
}