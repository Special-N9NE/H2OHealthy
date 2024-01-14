package org.n9ne.profile.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.BaseViewModel
import org.n9ne.common.model.Activity
import org.n9ne.common.util.DateUtils
import org.n9ne.common.util.Event
import org.n9ne.common.util.Mapper.toWater
import org.n9ne.common.util.RepoCallback
import org.n9ne.profile.repo.ProfileRepo
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class StatsViewModel @Inject constructor() : BaseViewModel<ProfileRepo>() {

    val ldStartDate = MutableLiveData<Event<String>>()
    val ldScore = MutableLiveData<Event<Int>>()
    val ldEmpty = MutableLiveData<Event<Unit>>()
    val ldAverage = MutableLiveData<Event<Double>>()
    val ldLineOverall = MutableLiveData<Event<List<Double>>>()
    val ldLineMonth = MutableLiveData<Event<List<Double>>>()
    val ldBarData = MutableLiveData<Event<List<Double>>>()
    val ldActivities = MutableLiveData<Event<List<Activity>>>()


    fun getActivities(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {

            repo?.getAllActivity(token, object : RepoCallback<List<Activity>> {
                override fun onSuccessful(response: List<Activity>) {

                    if (response.isEmpty()) {
                        ldEmpty.postValue(Event(Unit))
                        return
                    }

                    syncActivities(response)

                    val barValues = mutableListOf<Double>()
                    for (k in 0 until 24) {
                        var total = 0.0
                        response.forEach { j ->
                            val t2 = j.time.split(":")[0].toInt()
                            if (k == t2) {
                                total += j.amount.toDouble()
                            }
                        }

                        barValues.add(total)
                    }

                    val result = mutableListOf<Double>()
                    for (i in 0..(barValues.size - 2) step 2) {
                        result.add(barValues[i] + barValues[i + 1])
                    }

                    ldBarData.postValue(Event(result))


                    val startDate = LocalDate.parse(response[0].date.replace("/", "-"))
                    val endDate = LocalDate.parse(DateUtils.getDate().replace("/", "-"))

                    ldStartDate.postValue(Event(response[0].date))
                    val lineOverall = response.calculateDaysActivity(startDate, endDate)

                    val lineMonth = response.calculateDaysActivity(endDate.minusDays(30), endDate)
                    ldLineMonth.postValue(Event(lineMonth))

                    var average = 0.0
                    lineOverall.forEach {
                        average += (it * 100)
                    }
                    average /= lineOverall.size


                    var score = ((average * 100) / 3).toInt()
                    score /= 100
                    ldScore.postValue(Event(score))

                    average = average.roundToInt() / 100.0


                    ldAverage.postValue(Event(average))
                    ldLineOverall.postValue(Event(lineOverall))
                    ldActivities.postValue(Event(response))

                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }

            })
        }
    }

    private fun List<Activity>.calculateDaysActivity(
        startDate: LocalDate,
        endDate: LocalDate
    ): MutableList<Double> {

        val lineData = mutableListOf<Double>()
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            var total = 0.0
            this.forEach { j ->
                val date = j.date.replace("/", "-")
                if (date == currentDate.toString()) total += j.amount.toDouble()
            }
            lineData.add((total))
            currentDate = currentDate.plusDays(1)
        }

        return lineData
    }

    private fun syncActivities(list: List<Activity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()?.removeProgress()

                    list.forEach {
                        db?.roomDao()!!.insertWater(it.toWater())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}