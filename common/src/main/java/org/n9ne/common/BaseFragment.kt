package org.n9ne.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.n9ne.common.util.Utils.isOnline
import org.n9ne.common.util.interfaces.Navigator

abstract class BaseFragment<Repo> : Fragment(), Navigator {

    var apiRepo: Repo? = null
    var localRepo: Repo? = null
    private lateinit var viewModel: BaseViewModel<Repo>

    fun initRepos(api: Repo, local: Repo, viewModel: BaseViewModel<Repo>) {
        apiRepo = api
        localRepo = local
        this.viewModel = viewModel
    }

    fun makeRequest(request: () -> Unit) {
        val repo = if (requireActivity().isOnline()) {
            apiRepo
        } else {
            localRepo
        }
        viewModel.repo = repo
        request.invoke()
    }

    fun makeLocalRequest(request: () -> Unit) {
        viewModel.repo = localRepo
        request.invoke()
    }

    fun makeApiRequest(request: () -> Unit) {
        viewModel.repo = apiRepo
        request.invoke()
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}
