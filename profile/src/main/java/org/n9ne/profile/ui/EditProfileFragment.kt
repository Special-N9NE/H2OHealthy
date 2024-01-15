package org.n9ne.profile.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import org.n9ne.common.BaseFragment
import org.n9ne.common.R.color
import org.n9ne.common.model.ActivityType
import org.n9ne.common.model.UpdateUser
import org.n9ne.common.util.DateUtils.georgianToPersian
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.Utils
import org.n9ne.common.util.setUserAvatar
import org.n9ne.profile.R
import org.n9ne.profile.databinding.FragmentEditProfileBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.ui.viewModel.EditProfileViewModel

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<ProfileRepo, FragmentEditProfileBinding>() {

    private val viewModel: EditProfileViewModel by viewModels()
    private lateinit var date: String


    override fun getViewBinding(): FragmentEditProfileBinding =
        FragmentEditProfileBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideNavigation()

        createFragment()

        makeLocalRequest {
            viewModel.getUser(getToken())
        }
    }

    override fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)

        setupSpinners()
    }

    override fun setClicks() {
        b.bSubmit.setOnClickListener {
            b.bSubmit.isEnabled = false

            startLoading()
            makeApiRequest {

                val name = b.etName.text.toString()
                val email = b.etEmail.text.toString()
                val activityLevel = b.spActivity.text.toString()
                val gender = b.spGender.text.toString()
                val weight = b.etWeight.text.toString()
                val height = b.etHeight.text.toString()

                val user = UpdateUser(
                    activityLevel,
                    email,
                    name,
                    date,
                    weight,
                    height,
                    gender
                )
                makeApiRequest {
                    viewModel.saveData(user, getToken(), requireContext())
                }
            }
        }
        b.etBirthday.setOnClickListener {
            if (Utils.isLocalPersian())
                showPersianDateDialog()
            else
                showDateDialog()
        }
        b.ivProfile.setOnClickListener {
            this.shouldNavigate(R.id.editProfile_to_avatar)
        }
    }

    override fun setObservers() {
        viewModel.ldUser.observe(viewLifecycleOwner, EventObserver {
            b.user = it
            b.spActivity.setText(ActivityType.getLocalizedText(it.activityType, Utils.getLocal()))
            b.spGender.setText(it.gender)
            b.spActivity.setTextColor(resources.getColor(color.blackText, requireContext().theme))
            b.spGender.setTextColor(resources.getColor(color.blackText, requireContext().theme))
            setupSpinners()

            date = it.birthDate

            b.etBirthday.setText(
                if (Utils.isLocalPersian())
                    it.birthDate.georgianToPersian()
                else
                    it.birthDate
            )

            b.ivProfile.setUserAvatar(it.profile)
        })
        viewModel.ldSubmit.observe(viewLifecycleOwner, EventObserver(listOf(b.bSubmit)) {
            stopLoading()
            findNavController().navigateUp()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver(listOf(b.bSubmit)) {
            stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
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

    private fun setupSpinners() {
        val view = org.n9ne.common.R.layout.view_drop_down
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), view, viewModel.getActivityLevels()
        )
        b.spActivity.setAdapter(adapter)

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), view, viewModel.getGenders()
        )
        b.spGender.setAdapter(adapterGender)
    }

}