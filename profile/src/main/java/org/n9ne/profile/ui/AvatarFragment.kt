package org.n9ne.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class AvatarFragment : BaseFragment<ProfileRepo>() {
    private lateinit var b: FragmentAvatarsBinding
    private val viewModel: AvatarsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentAvatarsBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        baseActivity.hideNavigation()

        setupObserver()

        setAdapter()
    }

    private fun init() {
        viewModel.db = db

        b.viewModel = viewModel

        initRepos(apiRepo, localRepo, viewModel)
    }

    private fun setAdapter() {
        b.rv.adapter = AvatarAdapter(viewModel.avatars, object : AvatarClickListener {
            override fun onClick(image: String) {
                baseActivity.startLoading()
                makeApiRequest {
                    viewModel.saveProfile(getToken(), image)
                }
            }
        })
    }

    private fun setupObserver() {
        viewModel.ldSuccess.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            findNavController().navigateUp()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            baseActivity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }
}