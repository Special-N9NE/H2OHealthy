package org.n9ne.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.n9ne.common.BaseActivity
import org.n9ne.common.BaseFragment
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.network.Client
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.interfaces.AvatarClickListener
import org.n9ne.profile.databinding.FragmentAvatarsBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.repo.ProfileRepoApiImpl
import org.n9ne.profile.repo.ProfileRepoLocalImpl
import org.n9ne.profile.ui.adpter.AvatarAdapter
import org.n9ne.profile.ui.viewModel.AvatarsViewModel


class AvatarFragment : BaseFragment<ProfileRepo>() {
    private lateinit var b: FragmentAvatarsBinding
    private lateinit var viewModel: AvatarsViewModel
    private lateinit var activity: BaseActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentAvatarsBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        activity.hideNavigation()

        setupObserver()

        setAdapter()
    }

    private fun init() {
        activity = requireActivity() as BaseActivity

        val client = Client.getInstance()
        apiRepo = ProfileRepoApiImpl(client)
        localRepo = ProfileRepoLocalImpl(AppDatabase.getDatabase(requireContext()).roomDao())
        viewModel = ViewModelProvider(this)[AvatarsViewModel::class.java]
        viewModel.db = AppDatabase.getDatabase(requireContext())

        b.viewModel = viewModel

        initRepos(apiRepo as ProfileRepo, localRepo as ProfileRepo, viewModel)
    }

    private fun setAdapter() {
        b.rv.adapter = AvatarAdapter(viewModel.avatars, object : AvatarClickListener {
            override fun onClick(image: String) {
                activity.startLoading()
                makeApiRequest {
                    viewModel.saveProfile(getToken(), image)
                }
            }
        })
    }

    private fun setupObserver() {
        viewModel.ldSuccess.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            findNavController().navigateUp()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            activity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }
}