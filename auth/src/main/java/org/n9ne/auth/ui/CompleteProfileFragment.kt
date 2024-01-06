package org.n9ne.auth.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import org.n9ne.auth.R
import org.n9ne.auth.databinding.FragmentCompleteProfileBinding
import org.n9ne.auth.repo.AuthRepoImpl
import org.n9ne.auth.ui.viewModel.CompleteProfileViewModel
import org.n9ne.common.BaseActivity
import org.n9ne.common.R.color
import org.n9ne.common.R.layout
import org.n9ne.common.source.network.Client
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.saveEmail
import org.n9ne.common.util.Saver.saveToken
import org.n9ne.common.util.Utils
import org.n9ne.common.util.interfaces.Navigator


class CompleteProfileFragment : Fragment(), Navigator {

    private lateinit var b: FragmentCompleteProfileBinding
    private lateinit var viewModel: CompleteProfileViewModel
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    private var date: String = ""
    private lateinit var activity: BaseActivity

    private var genders = listOf<String>()
    var genderSelected = false
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
        activity = requireActivity() as BaseActivity
        val client = Client.getInstance()
        val repo = AuthRepoImpl(client)

        viewModel = ViewModelProvider(this)[CompleteProfileViewModel::class.java]
        viewModel.navigator = this

        viewModel.repo = repo
        b.viewModel = viewModel

        genders = listOf(
            getString(org.n9ne.common.R.string.male),
            getString(org.n9ne.common.R.string.female),
        )
    }

    private fun setClicks() {
        b.bNext.setOnClickListener {

            if (!genderSelected) {
                val error = if (Utils.isLocalPersian())
                    "جنسیت را انتخاب کنید"
                else
                    "Choose gender"

                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            } else {

                b.bNext.isEnabled = false

                activity.startLoading()
                viewModel.completeProfile(
                    name, email, password, b.spActivity.text.toString(), b.spGender.text.toString(),
                    date, b.etWeight.text.toString(), b.etHeight.text.toString(),
                    requireContext()
                )
            }
        }
    }

    private fun setupSpinners() {
        val view = layout.view_drop_down
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            view, viewModel.getActivityLevels()
        )
        b.spActivity.setAdapter(adapter)

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            view, genders
        )
        b.spGender.setAdapter(adapterGender)
        b.spGender.doOnTextChanged { _, _, _, _ ->
            genderSelected = true
        }
    }

    private fun setObservers() {
        viewModel.ldUserToken.observe(viewLifecycleOwner, EventObserver(listOf(b.bNext)) {
            saveEmail(email)
            saveToken(it)

            val data = Bundle().apply {
                putString("name", name)
            }
            this.shouldNavigate(R.id.completeProfile_to_loginDone, data)

            activity.stopLoading()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver(listOf(b.bNext)) {
            activity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
        viewModel.ldShowDate.observe(viewLifecycleOwner, EventObserver {
            if (Utils.isLocalPersian())
                showPersianDateDialog()
            else
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
            .setTodayButtonVisible(true)
            .setMinYear(1300)
            .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
            .setMaxMonth(PersianDatePickerDialog.THIS_MONTH)
            .setMaxDay(PersianDatePickerDialog.THIS_DAY)
            .setActionTextColor(resources.getColor(color.linearBlueEnd, requireContext().theme))
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