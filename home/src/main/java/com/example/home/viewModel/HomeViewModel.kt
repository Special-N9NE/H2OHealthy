package com.example.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.home.repo.HomeRepo
import com.example.home.repo.HomeRepoApiImpl
import com.example.home.repo.HomeRepoLocalImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.BaseViewModel
import org.n9ne.common.model.Activity
import org.n9ne.common.model.GoalHistoryPage
import org.n9ne.common.model.GoalMath
import org.n9ne.common.model.GoalState
import org.n9ne.common.model.GoalUpdate
import org.n9ne.common.model.Progress
import org.n9ne.common.source.local.GoalCacheEntity
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toLiter
import org.n9ne.common.util.Mapper.toMilliLiter
import org.n9ne.common.util.Mapper.toWater
import org.n9ne.common.util.RepoCallback
import org.n9ne.common.util.Utils
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeRepo>() {
    var target: Int? = null

    val ldTarget = MutableLiveData<Int>()
    val ldDayProgress = MutableLiveData<Int>()
    val ldWeekProgress = MutableLiveData<List<Progress>>()
    val ldActivities = MutableLiveData<List<Activity>>()
    val ldAllActivities = MutableLiveData<List<Activity>>()
    val ldGoals = MutableLiveData<GoalState>()
    val ldGoalsEditable = MutableLiveData(false)
    val ldGoalSaving = MutableLiveData(false)
    val ldGoalSaved = MutableLiveData<Event<Unit>>()
    val ldGoalSaveError = MutableLiveData<Event<String>>()
    val historyStart = MutableLiveData<LocalDate>()
    private var pendingGoal: GoalUpdate? = null

    @Volatile
    private var latestGoals: GoalState? = null

    fun getGoalHistory(
        token: String?,
        before: Long?,
        callback: (GoalHistoryPage?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentRepo = repo
            if (currentRepo == null) {
                withContext(Dispatchers.Main) { callback(null) }; return@launch
            }
            currentRepo.getGoalHistory(
                token,
                before,
                object : RepoCallback<GoalHistoryPage> {
                    override fun onSuccessful(response: GoalHistoryPage) {
                        viewModelScope.launch(Dispatchers.Main) { callback(response) }
                    }

                    override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                        viewModelScope.launch(Dispatchers.Main) { callback(null) }
                    }
                })
        }
    }

    fun getActivityHistory(
        token: String?,
        start: LocalDate,
        end: LocalDate,
        callback: (List<Activity>?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentRepo = repo
            if (currentRepo == null) {
                withContext(Dispatchers.Main) { callback(null) }
                return@launch
            }
            currentRepo.getActivityHistory(
                token,
                start,
                end,
                object : RepoCallback<List<Activity>> {
                    override fun onSuccessful(response: List<Activity>) {
                        viewModelScope.launch(Dispatchers.Main) { callback(response) }
                    }

                    override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                        viewModelScope.launch(Dispatchers.Main) { callback(null) }
                    }
                })
        }
    }

    fun getTarget(token: String?) {

        viewModelScope.launch(Dispatchers.IO) {
            val online = repo is HomeRepoApiImpl
            repo?.getGoals(token, object : RepoCallback<GoalState> {
                override fun onSuccessful(response: GoalState) {
                    applyGoals(response, online)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldGoalsEditable.postValue(false)
                    viewModelScope.launch(Dispatchers.IO) {
                        db?.roomDao()?.let { dao ->
                            HomeRepoLocalImpl(dao).getGoals(
                                token,
                                object : RepoCallback<GoalState> {
                                    override fun onSuccessful(response: GoalState) {
                                        applyGoals(response, false)
                                    }

                                    override fun onError(
                                        error: String,
                                        isNetwork: Boolean,
                                        isToken: Boolean
                                    ) {
                                        ldError.postValue(Event(error))
                                    }
                                })
                        }
                    }
                }
            })
        }
    }

    fun saveGoal(litres: Int, tomorrow: Boolean, token: String?) {
        if (ldGoalSaving.value == true) return
        val state = ldGoals.value ?: return
        if (ldGoalsEditable.value != true) {
            ldGoalSaveError.value = Event("goals_unavailable")
            return
        }
        val date = LocalDate.now().plusDays(if (tomorrow) 1 else 0).toString()
        val previous = pendingGoal
        val update =
            if (previous?.target == litres && previous.effectiveDate == date && previous.revision == state.revision) {
                previous
            } else GoalUpdate(
                litres,
                date,
                ZoneId.systemDefault().id,
                state.revision,
            )
        pendingGoal = update
        ldGoalSaving.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repo?.saveGoal(token, update, object : RepoCallback<GoalState> {
                override fun onSuccessful(response: GoalState) {
                    pendingGoal = null
                    applyGoals(response, true)
                    ldGoalSaving.postValue(false)
                    ldGoalSaved.postValue(Event(Unit))
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldGoalSaving.postValue(false)
                    ldGoalSaveError.postValue(Event(error))
                    if (error == "goal_conflict" || error == "invalid_goal") {
                        pendingGoal = null
                        getTarget(token)
                    }
                }
            })
        }
    }

    @Synchronized
    private fun applyGoals(state: GoalState, editable: Boolean) {
        ldGoalsEditable.postValue(editable)
        val previous = latestGoals
        if (previous?.userId == state.userId && previous.revision > state.revision) return
        latestGoals = state
        val today = LocalDate.now()
        val current = state.targetOn(today)
        db?.runInTransaction {
            val dao = db!!.roomDao()
            val user = dao.getUser().firstOrNull()
            user?.let { GoalMath.date(it.date)?.let(historyStart::postValue) }
            if (user?.id == state.userId) {
                dao.cacheGoals(GoalCacheEntity(state.userId, com.google.gson.Gson().toJson(state)))
                dao.updateUserTarget(state.userId, current.toString())
            }
        }
        target = current
        ldGoalsEditable.postValue(editable)
        ldGoals.postValue(state)
        ldTarget.postValue(current)
        ldAllActivities.value?.let { renderProgress(it, state) }
    }

    private fun renderProgress(activities: List<Activity>, state: GoalState? = latestGoals) {
        val today = LocalDate.now()
        ldDayProgress.postValue(
            GoalMath.percentage(
                Utils.calculateDayProgress(activities).toDouble(),
                state?.targetOn(today) ?: target ?: 3
            )
        )
        val week = Utils.calculateWeekProgress(activities)
        week.forEach {
            val date = GoalMath.date(it.date) ?: today
            it.progress = GoalMath.percentage(it.progress.toDouble(), state?.targetOn(date) ?: 3)
        }
        ldWeekProgress.postValue(week)
    }

    fun getProgress(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.getProgress(token, object : RepoCallback<List<Activity>> {
                override fun onSuccessful(response: List<Activity>) {

                    renderProgress(response)
                    ldAllActivities.postValue(response)
                    ldActivities.postValue(Utils.calculateActivities(response))

                    syncProgress(response)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }


    fun updateWater(activity: Activity, token: String?) {
        if (activity.amount.toDouble() >= 3) {
            val error =
                if (Utils.isLocalPersian())
                    "مقدار بالاتر از حد مجاز است"
                else
                    "amount is too high"
            ldError.postValue(Event(error))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            repo?.updateWater(activity.id!!, activity.amount, token, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {

                    val activities = ldActivities.value!!

                    activities.forEach { item ->
                        if (item.id == activity.id) item.amount =
                            activity.amount.toDouble().toMilliLiter().roundToInt().toString()
                    }

                    var progress = 0.0
                    activities.forEach { item ->
                        progress += (item.amount.toDouble())
                    }
                    progress = (100 * progress) / (target!!.toDouble().toMilliLiter())

                    val score = calculateScore(activities)
                    syncScore(score)
                    syncActivity(activity, false)

                    ldDayProgress.postValue(progress.roundToInt())
                    ldActivities.postValue(activities)
                    getProgress(token)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    fun removeWater(activity: Activity, token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.removeWater(activity.id!!, token, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {
                    val activities = ldActivities.value!!.toCollection(ArrayList())

                    activities.removeIf { it.id == activity.id }


                    val list = arrayListOf<Activity>()
                    activities.forEach {
                        list.add(
                            Activity(
                                it.id,
                                it.idUser,
                                (it.amount.toDouble().toLiter()).toString(),
                                it.date,
                                ""
                            )
                        )
                    }

                    val score = calculateScore(activities)
                    syncScore(score)
                    syncActivity(activity, true)

                    var progress = Utils.calculateDayProgress(list.toList())
                    progress = (100 * progress) / (target!!.toDouble().toMilliLiter().toInt())
                    ldDayProgress.postValue(progress)
                    ldActivities.postValue(activities)
                    getProgress(token)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }

    private fun syncScore(score: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()!!.updateScore(score.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun syncActivity(data: Activity, deleted: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (deleted) {
                        db?.roomDao()!!.removeWater(data.id!!)
                    } else {
                        db?.roomDao()!!.updateWater(data.id!!, data.amount)

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun syncProgress(list: List<Activity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.runInTransaction {
                        db?.roomDao()?.removeProgress()
                        list.forEach { db?.roomDao()?.insertWater(it.toWater()) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun calculateScore(list: List<Activity>): Int {
        var total = 0.0
        list.forEach {
            total += it.amount.toDouble()
        }

        total = total.toLiter()
        return (total / 3).roundToInt()
    }
}
