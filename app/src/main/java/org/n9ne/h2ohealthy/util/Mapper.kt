package org.n9ne.h2ohealthy.util

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.source.local.GlassEntity
import org.n9ne.h2ohealthy.data.source.local.UserEntity
import org.n9ne.h2ohealthy.data.source.local.WaterEntity
import org.n9ne.h2ohealthy.data.source.objects.Login

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
        val genderText = if (gender == 1) "Male" else "Female"

        return User(
            id,
            activity,
            idLeague,
            email,
            password,
            name,
            date,
            target,
            age.toString(),
            birthdate,
            weight,
            height,
            genderText,
            score,
            profile
        )
    }

    fun Login.toUser(): User {

        val user = this.user!![0]

        val activity = ActivityType.entries[user.idActivity - 1]
        val age = DateUtils.calculateAge(user.birthdate)
        val genderText = if (user.gender == 1) "Male" else "Female"

        return User(
            user.id.toLong(),
            activity,
            user.idleague.toLong(),
            user.email,
            user.password,
            user.name,
            user.date,
            user.target,
            age.toString(),
            user.birthdate,
            user.weight.toString(),
            user.height.toString(),
            genderText,
            user.score,
            user.profile
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