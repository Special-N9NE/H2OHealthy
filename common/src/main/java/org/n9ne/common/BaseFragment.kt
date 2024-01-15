package org.n9ne.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.n9ne.common.di.LocalRepo
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.Utils.isOnline
import org.n9ne.common.util.interfaces.Navigator
import javax.inject.Inject

abstract class BaseFragment<Repo : Any, B : ViewDataBinding> : Fragment(), Navigator {


    protected lateinit var b: B

    @Inject
    protected lateinit var apiRepo: Repo

    @Inject
    protected lateinit var baseActivity: BaseActivity

    @Inject
    protected lateinit var db: AppDatabase

    @LocalRepo
    @Inject
    protected lateinit var localRepo: Repo


    private lateinit var viewModel: BaseViewModel<Repo>

    abstract fun getViewBinding(): B
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = getViewBinding()
        return b.root
    }

    abstract fun init()
    abstract fun setClicks()
    abstract fun setObservers()
    protected fun createFragment() {
        stopLoading()

        init()
        setClicks()
        setObservers()
    }

    fun startLoading() = baseActivity.startLoading()

    fun stopLoading() = baseActivity.stopLoading()

    fun showNavigation() = baseActivity.showNavigation()

    fun hideNavigation() = baseActivity.hideNavigation()


    fun initRepos(viewModel: BaseViewModel<Repo>) {
        this.viewModel = viewModel
        viewModel.db = db
        viewModel.navigator = this
    }

    fun makeRequest(request: () -> Unit) {
        startLoading()
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
        startLoading()
        viewModel.repo = apiRepo
        request.invoke()
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}