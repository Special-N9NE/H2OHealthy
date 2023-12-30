package org.n9ne.profile.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import org.n9ne.common.BaseActivity
import org.n9ne.common.BaseFragment
import org.n9ne.common.R.color
import org.n9ne.common.model.UpdateUser
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.source.network.Client
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.interfaces.Navigator
import org.n9ne.common.util.setUserAvatar
import org.n9ne.profile.R
import org.n9ne.profile.databinding.FragmentEditProfileBinding
import org.n9ne.profile.repo.ProfileRepo
import org.n9ne.profile.repo.ProfileRepoApiImpl
import org.n9ne.profile.repo.ProfileRepoLocalImpl
import org.n9ne.profile.ui.viewModel.EditProfileViewModel


class EditProfileFragment : BaseFragment<ProfileRepo>(), Navigator {

    private lateinit var b: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var date: String
    private lateinit var activity: BaseActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentEditProfileBinding.inflate(inflater)
        return b.root
    }
    //TODO set some default profiles so user can pick it

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        activity.hideNavigation()

        setupObserver()
        setClicks()

        makeLocalRequest {
            viewModel.getUser(requireActivity().getToken())
        }
    }

    private fun setClicks() {
        b.bSubmit.setOnClickListener {
            b.bSubmit.isEnabled = false

            activity.startLoading()
            makeApiRequest {

                val name = b.etName.text.toString()
                val email = b.etEmail.text.toString()
                val activityLevel = b.spActivity.text.toString()
                val gender = b.spGender.text.toString()
                val birthdate = b.etBirthday.text.toString()
                val weight = b.etWeight.text.toString()
                val height = b.etHeight.text.toString()

                val user = UpdateUser(
                    activityLevel,
                    email,
                    name,
                    birthdate,
                    weight,
                    height,
                    gender
                )
                makeApiRequest {
                    viewModel.saveData(user, requireActivity().getToken())
                }
            }
        }
        b.etBirthday.setOnClickListener {
            showDateDialog()
        }
        b.ivProfile.setOnClickListener {
            this.shouldNavigate(R.id.editProfile_to_avatar)
        }
    }

    private fun init() {
        activity = requireActivity() as BaseActivity
        val client = Client.getInstance()

        localRepo = ProfileRepoLocalImpl(AppDatabase.getDatabase(requireContext()).roomDao())
        apiRepo = ProfileRepoApiImpl(client)

        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
        viewModel.db = AppDatabase.getDatabase(requireContext())
        b.viewModel = viewModel

        initRepos(apiRepo as ProfileRepo, localRepo as ProfileRepo, viewModel)

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
            requireContext(), view, viewModel.activityLevels
        )
        b.spActivity.setAdapter(adapter)

        val adapterGender: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), view, viewModel.genders
        )
        b.spGender.setAdapter(adapterGender)
    }

    private fun setupObserver() {
        viewModel.ldUser.observe(viewLifecycleOwner, EventObserver {
            b.user = it
            b.spActivity.setText(it.activityType.text)
            b.spGender.setText(it.gender)
            b.spActivity.setTextColor(resources.getColor(color.blackText, requireContext().theme))
            b.spGender.setTextColor(resources.getColor(color.blackText, requireContext().theme))
            setupSpinners()

            b.ivProfile.setUserAvatar(it.profile)

            date = it.birthDate
        })
        viewModel.ldSubmit.observe(viewLifecycleOwner, EventObserver(listOf(b.bSubmit)) {
            activity.stopLoading()
            findNavController().navigateUp()
        })
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver(listOf(b.bSubmit)) {
            activity.stopLoading()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

    }
}