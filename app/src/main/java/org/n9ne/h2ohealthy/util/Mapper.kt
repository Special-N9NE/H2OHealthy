package org.n9ne.h2ohealthy.util

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.local.UserEntity
import org.n9ne.h2ohealthy.data.repo.local.WaterEntity

object Mapper {
    fun List<WaterEntity>.toActivityList(): ArrayList<Activity> {
        val result = arrayListOf<Activity>()
        this.forEach {
            result.add(Activity(it.id, it.idUser, it.amount, it.date, it.time))
        }
        return result
    }

    fun Activity.toWater(): WaterEntity {
        return WaterEntity(idUser, date, amount, time)
    }

    fun UserEntity.toUser(): User {

        val activity = ActivityType.entries[idActivity.toInt()]
        val age = DateUtils.calculateAge(birthdate)
        return User(
            id,
            activity,
            idLeague,
            email,
            name,
            age.toString(),
            birthdate,
            weight,
            height,
            score,
            profile
        )
    }


}