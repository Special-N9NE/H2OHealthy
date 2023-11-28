package org.n9ne.h2ohealthy.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.data.repo.profile.ProfileRepoLocalImpl
import org.n9ne.h2ohealthy.data.source.local.AppDatabase
import org.n9ne.h2ohealthy.databinding.FragmentEditProfileBinding
import org.n9ne.h2ohealthy.ui.MainActivity
import org.n9ne.h2ohealthy.ui.profile.viewModel.EditProfileViewModel
import org.n9ne.h2ohealthy.util.EventObserver
import org.n9ne.h2ohealthy.util.Utils.isOnline
import org.n9ne.h2ohealthy.util.interfaces.Navigator


class EditProfileFragment : Fragment(), Navigator {

    private lateinit var b: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var localRepo: ProfileRepoLocalImpl
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentEditProfileBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).showNavigation()
        init()

        setupObserver()
        setClicks()

        makeLocalRequest {
            viewModel.getUser()
        }
    }

    private fun setClicks() {
        b.bSubmit.setOnClickListener {
            b.bSubmit.isEnabled = false
            makeRequest {
                viewModel.saveData()
            }
        }
        b.etBirthday.setOnClickListener {
            showDateDialog()
        }
        b.ivProfile.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }

    private fun init() {
        localRepo = ProfileRepoLocalImpl(AppDatabase.getDatabase(requireContext()).roomDao())
        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        b.viewModel = viewModel

        setupSpinners()
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

    private fun makeRequest(request: () -> Unit) {
        val repo = if (requireActivity().isOnline()) {
            //TODO pass apiRepo
            localRepo
        } else {
            localRepo
        }
        viewModel.repo = repo
        request.invoke()
    }

    private fun makeLocalRequest(request: () -> Unit) {
        viewModel.repo = localRepo
        request.invoke()
    }

    private fun setupSpinners() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.view_drop_down, viewModel.activityLevels
        )
        b.spActivity.setAdapter(adapter)

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.view_drop_down, viewModel.genders
        )
        b.spGender.setAdapter(adapterGender)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            b.ivProfile.setImageURI(galleryUri)
            //TODO save image
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupObserver() {
        viewModel.ldUser.observe(viewLifecycleOwner, EventObserver {
            b.user = it
            b.spActivity.setText(it.activityType.text)
            b.spGender.setText(it.gender)
            b.spActivity.setTextColor(resources.getColor(R.color.blackText, requireContext().theme))
            b.spGender.setTextColor(resources.getColor(R.color.blackText, requireContext().theme))
            setupSpinners()

            date = it.birthDate
        })
        viewModel.ldSubmit.observe(viewLifecycleOwner, EventObserver(b.bSubmit) {
            findNavController().navigateUp()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver(b.bSubmit) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

    }

    override fun shouldNavigate(destination: Int, data: Bundle?) {
        findNavController().navigate(destination, data)
    }

}