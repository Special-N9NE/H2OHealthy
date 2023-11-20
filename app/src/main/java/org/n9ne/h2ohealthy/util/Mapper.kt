package org.n9ne.h2ohealthy.util

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.repo.local.GlassEntity
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

    fun List<GlassEntity>.toCupList(): ArrayList<Cup> {
        val result = arrayListOf<Cup>()
        this.forEach {
            result.add(Cup(it.id, it.idUser, it.name, it.capacity.toInt(), it.color))
        }
        return result
    }

    fun Cup.toGlass(): GlassEntity {
        return GlassEntity(idUser, title, capacity.toString(), color)
    }

    fun Double.toLiter(): Double {
        return this / 1000.000
    }

    fun Double.toMilliLiter(): Double {
        return this * 1000
    }
}