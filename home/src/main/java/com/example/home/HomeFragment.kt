package com.example.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.home.adpter.ActivityAdapter
import com.example.home.databinding.FragmentHomeBinding
import com.example.home.repo.HomeRepo
import com.example.home.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.n9ne.common.BaseFragment
import org.n9ne.common.dialog.activityOptionDialog
import org.n9ne.common.model.Activity
import org.n9ne.common.model.GoalMath
import org.n9ne.common.model.Progress
import org.n9ne.common.util.EventObserver
import org.n9ne.common.util.Mapper.toLiter
import org.n9ne.common.util.Saver.getToken
import org.n9ne.common.util.Utils.isOnline
import org.n9ne.common.util.errorToast
import org.n9ne.common.util.interfaces.AddWaterListener
import org.n9ne.common.util.interfaces.MenuClickListener
import org.n9ne.common.util.interfaces.RefreshListener
import org.n9ne.common.util.interfaces.RemoveActivityListener
import org.n9ne.common.util.successToast
import org.nine.linearprogressbar.LinearVerticalProgressBar
import java.time.LocalDate
import org.n9ne.common.R as CommonR

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeRepo, FragmentHomeBinding>(), RefreshListener {

    private val viewModel: HomeViewModel by viewModels()

    private var activityList = arrayListOf<Activity>()
    private var goalEditor: GoalEditor? = null
    private var goalHistory: GoalHistoryDialog? = null
    private var displayedDate = LocalDate.now()
    private var lastOnline: Boolean? = null
    private val dayHandler = Handler(Looper.getMainLooper())
    private val dayCheck = object : Runnable {
        override fun run() {
            if (isAdded && LocalDate.now() != displayedDate) onRefresh()
            dayHandler.postDelayed(this, 30_000)
        }
    }
    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isResumed && getToken() != null) {
                val online = requireContext().isOnline()
                if (online != lastOnline) {
                    lastOnline = online; onRefresh()
                }
            }
        }
    }

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createFragment()

        showNavigation()

        if (getToken() == null) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("h2ohealthy://auth")
            )
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun init() {
        b.viewModel = viewModel

        initRepos(viewModel)
    }

    override fun setClicks() {
        b.goalCard.btnEditGoal.setOnClickListener { openGoalEditor() }
        b.goalCard.btnGoalHistory.setOnClickListener { openGoalHistory() }
        b.btnActivityHistory.setOnClickListener { openGoalHistory(true) }
    }

    private fun openGoalEditor() {
        val state = viewModel.ldGoals.value ?: return
        goalEditor?.dialog?.dismiss()
        goalEditor = GoalEditor(requireContext(), state, false) { litres, fromTomorrow ->
            if (!requireContext().isOnline()) {
                goalEditor?.showError(getString(CommonR.string.goal_offline))
            } else
                viewModel.saveGoal(litres, fromTomorrow, getToken())
        }.also {
            it.update(state, viewModel.ldGoalsEditable.value == true && requireContext().isOnline())
            it.show()
            if (!requireContext().isOnline()) it.showError(getString(CommonR.string.goal_offline))
            it.setSaving(viewModel.ldGoalSaving.value == true)
        }
    }

    private fun openGoalHistory(activitiesHistory: Boolean = false) {
        val state = viewModel.ldGoals.value ?: return
        val activities = viewModel.ldAllActivities.value.orEmpty()
        val earliest = (activities.mapNotNull { GoalMath.date(it.date) } +
                state.changes.mapNotNull { GoalMath.date(it.effectiveDate) } +
                listOfNotNull(viewModel.historyStart.value)).minOrNull()
            ?.coerceAtMost(LocalDate.now()) ?: LocalDate.now()
        goalHistory?.dismiss()
        goalHistory = GoalHistoryDialog(
            requireContext(),
            state,
            activities,
            earliest,
            !requireContext().isOnline() || viewModel.ldGoalsEditable.value != true,
            activitiesHistory,
            { start, end, callback ->
                viewModel.getActivityHistory(
                    getToken(),
                    start,
                    end,
                    callback
                )
            },
            { cursor, callback -> viewModel.getGoalHistory(getToken(), cursor, callback) }
        ).also { it.show() }
    }

    override fun setObservers() {
        viewModel.ldGoals.observe(viewLifecycleOwner) { state ->
            goalEditor?.update(
                state,
                viewModel.ldGoalsEditable.value == true && requireContext().isOnline()
            )
            goalHistory?.update(state, viewModel.ldAllActivities.value.orEmpty())
        }
        viewModel.ldGoalsEditable.observe(viewLifecycleOwner) { editable ->
            viewModel.ldGoals.value?.let {
                goalEditor?.update(
                    it,
                    editable && requireContext().isOnline()
                )
            }
        }
        viewModel.ldAllActivities.observe(viewLifecycleOwner) { activities ->
            viewModel.ldGoals.value?.let { goalHistory?.update(it, activities) }
        }
        viewModel.ldGoalSaving.observe(viewLifecycleOwner) { goalEditor?.setSaving(it) }
        viewModel.ldGoalSaved.observe(viewLifecycleOwner, EventObserver {
            goalEditor?.dialog?.dismiss()
            requireContext().successToast(getString(CommonR.string.goal_saved))
        })
        viewModel.ldGoalSaveError.observe(viewLifecycleOwner, EventObserver { error ->
            val text = getString(
                when (error) {
                    "goal_conflict" -> CommonR.string.goal_conflict
                    "invalid_goal" -> CommonR.string.goal_invalid
                    "goals_network" -> CommonR.string.goal_network
                    else -> CommonR.string.goal_unavailable
                }
            )
            goalEditor?.showError(text)
        })
        viewModel.ldTarget.observe(viewLifecycleOwner) {
            b.goalCard.tvTarget.text = requireContext().goalLitres(it)

            makeRequest {
                viewModel.getProgress(getToken())
            }
        }
        viewModel.ldDayProgress.observe(viewLifecycleOwner) {
            b.goalCard.pbTarget.setProgress(it.coerceIn(0, 100))
        }
        viewModel.ldWeekProgress.observe(viewLifecycleOwner) {
            setProgress(it)
        }
        viewModel.ldActivities.observe(viewLifecycleOwner) {
            activityList = it.toCollection(ArrayList())
            setActivityAdapter(it)

            stopLoading()
        }
        viewModel.ldError.observe(viewLifecycleOwner, EventObserver {
            stopLoading()
            requireContext().errorToast(it)
        })
    }

    private fun setActivityAdapter(list: List<Activity>) {
        val adapter = ActivityAdapter(list, object : MenuClickListener {
            override fun onMenuClick(item: Activity) {
                val editListener = object : AddWaterListener {
                    override fun onAdd(amount: String) {
                        item.amount = amount.toDouble().toLiter().toString()

                        makeApiRequest {
                            viewModel.updateWater(item, getToken())
                        }
                    }
                }
                val removeListener = object : RemoveActivityListener {
                    override fun onRemove() {

                        makeApiRequest {
                            viewModel.removeWater(item, getToken())
                        }
                    }
                }
                val dialog =
                    requireActivity().activityOptionDialog(item, editListener, removeListener)
                dialog.show()
            }
        })
        b.rvActivity.adapter = adapter
    }

    private fun setProgress(daily: List<Progress>) {
        for ((index, i) in (0 until b.clProgress.childCount step 2).withIndex()) {
            val v = b.clProgress.getChildAt(i)
            (v as LinearVerticalProgressBar).setProgress(daily[index].progress.coerceIn(0, 100))
        }
        for ((index, i) in (1 until b.clProgress.childCount step 2).withIndex()) {
            val v = b.clProgress.getChildAt(i)
            (v as TextView).text = daily[index].day
        }
    }


    override fun onRefresh() {
        displayedDate = LocalDate.now()
        makeRequest {
            viewModel.getTarget(getToken())
        }
    }

    override fun onResume() {
        super.onResume()
        lastOnline = requireContext().isOnline()
        dayHandler.postDelayed(dayCheck, 30_000)
        ContextCompat.registerReceiver(
            requireContext(),
            connectivityReceiver,
            IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        if (getToken() != null) onRefresh()
    }

    override fun onPause() {
        dayHandler.removeCallbacks(dayCheck)
        requireContext().unregisterReceiver(connectivityReceiver)
        super.onPause()
    }

    override fun onDestroyView() {
        goalEditor?.dialog?.dismiss()
        goalHistory?.dismiss()
        goalEditor = null
        goalHistory = null
        super.onDestroyView()
    }
}
