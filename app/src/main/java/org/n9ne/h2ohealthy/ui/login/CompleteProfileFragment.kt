package org.n9ne.h2ohealthy.ui.login

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import org.n9ne.h2ohealthy.App
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.auth.AuthRepoImpl
import org.n9ne.h2ohealthy.databinding.FragmentCompleteProfileBinding
import org.n9ne.h2ohealthy.ui.login.viewModel.CompleteProfileViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Saver.saveToken
import org.n9ne.h2ohealthy.util.interfaces.Navigator


class CompleteProfileFragment : Fragment(), Navigator {

    private lateinit var b: FragmentCompleteProfileBinding
    private lateinit var viewModel: CompleteProfileViewModel
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var date: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentCompleteProfileBinding.inflate(inflater)
        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = requireArguments().getString("name")!!
        email = requireArguments().getString("email")!!
        password = requireArguments().getString("password")!!

        init()
        setupSpinners()
        setObservers()

        setClicks()
    }

    private fun init() {
        val client = (requireActivity().application as App).client
        val repo = AuthRepoImpl(client)

        viewModel = ViewModelProvider(this)[CompleteProfileViewModel::class.java]
        viewModel.navigator = this

        viewModel.repo = repo
        b.viewModel = viewModel
    }

    private fun setClicks() {
        b.bNext.setOnClickListener {
            b.bNext.isEnabled = false

            viewModel.completeProfile(
                name, email, password, b.spActivity.text.toString(), b.spGender.text.toString(),
                date, b.etWeight.text.toString(), b.etHeight.text.toString(),
                requireContext()
            )
        }
    }

    private fun setupSpinners() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.view_drop_down, viewModel.activityLevels
        )
        b.spActivity.setAdapter(adapter)

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.view_drop_down, viewModel.genders
        )
        b.spGender.setAdapter(adapterGender)
    }

    private fun setObservers() {
        viewModel.ldToken.observe(viewLifecycleOwner, EventObserver(listOf(b.bNext)) {
            requireActivity().saveToken(it)
            val data = Bundle().apply {
                putString("name", name)
            }
            this.shouldNavigate(R.id.completeProfile_to_loginDone , data)
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver(listOf(b.bNext)) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
        viewModel.ldShowDate.observe(viewLifecycleOwner, EventObserver {
            showDateDialog()
        })
    }

    private fun showDateDialog() {
        var initYear = 2001
        var initMonth = 10
        var initDay = 18

        val text = b.etBirthday.text.toString().trim()
        if (text.isNotEmpty()) {
            val split = text.split("/")
            initYear = split[0].toInt()
            initMonth = split[1].toInt() - 1
            initDay = split[2].toInt()
        }
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                date = "$year/${month + 1}/$dayOfMonth"
                b.etBirthday.setText(date)
            }, initYear, initMonth, initDay
        ).show()

    }

    private fun showPersianDateDialog() {

        PersianDatePickerDialog(requireContext())
            .setPositiveButtonString("تایید")
            .setNegativeButton("لغو")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setMinYear(1300)
            .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
            .setMaxMonth(PersianDatePickerDialog.THIS_MONTH)
            .setMaxDay(PersianDatePickerDialog.THIS_DAY)
            .setInitDate(1380, 7, 26)
            .setActionTextColor(resources.getColor(R.color.linearBlueEnd, requireContext().theme))
//            .setTypeFace(
//                Typeface.createFromAsset(
//                    requireContext().assets,
//                    "font/poppins_regular.ttf"
//                )
//            )
            .setShowInBottomSheet(true)
            .setListener(object : PersianPickerListener {
                override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                    date =
                        "${persianPickerDate.gregorianYear}/${persianPickerDate.gregorianMonth}/${persianPickerDate.gregorianDay}"
                    val pDate =
                        "${persianPickerDate.persianYear}/${persianPickerDate.persianMonth}/${persianPickerDate.persianDay}"
                    b.etBirthday.setText(pDate)
                }

                override fun onDismissed() {}
            }).show()
    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }
}