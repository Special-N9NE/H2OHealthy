package org.n9ne.h2ohealthy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.databinding.FragmentCupsBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.dialog.addCupDialog
import org.n9ne.h2ohealthy.ui.profile.adpter.AddCupAdapter
import org.n9ne.h2ohealthy.ui.profile.viewModel.CupsViewModel
import org.n9ne.h2ohealthy.util.interfaces.CupClickListener
import org.n9ne.h2ohealthy.util.interfaces.CupEditListener


class CupsFragment : Fragment() {

    private lateinit var b: FragmentCupsBinding
    private lateinit var viewModel: CupsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentCupsBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).hideNavigation()
        init()

        setupObserver()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[CupsViewModel::class.java]
        b.viewModel = viewModel

        setAdapter()
    }

    private fun setAdapter() {
        b.rvCups.adapter = AddCupAdapter(viewModel.cups, object : CupEditListener {
            override fun onEdit(cup: Cup, edit: Boolean) {
                if (edit) {
                    requireActivity().addCupDialog(cup, object : CupClickListener {
                        override fun onClick(item: Cup) {
                            //TODO update database
                        }
                    }).show()
                }else{
                    //TODO remove database
                }
            }
        })
    }

    private fun setupObserver() {
        viewModel.ldAddClick.observe(viewLifecycleOwner) {
            if (it.notHandled) {
                if (viewModel.cups.size <= 5) {
                    requireActivity().addCupDialog(null, object : CupClickListener {
                        override fun onClick(item: Cup) {
                            //TODO insert cup
                        }
                    }).show()
                } else {
                    //TODO 6 cup limit error
                }
            }
        }
    }
}