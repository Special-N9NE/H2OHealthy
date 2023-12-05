package org.n9ne.h2ohealthy.util

import org.n9ne.h2ohealthy.data.model.Activity
import org.n9ne.h2ohealthy.data.model.ActivityType
import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.data.source.local.GlassEntity
import org.n9ne.h2ohealthy.data.source.local.UserEntity
import org.n9ne.h2ohealthy.data.source.local.WaterEntity
import org.n9ne.h2ohealthy.data.source.objects.GetCups
import org.n9ne.h2ohealthy.data.source.objects.GetProgress
import org.n9ne.h2ohealthy.data.source.objects.GetUser

object Mapper {

    fun List<GetProgress.Data>.toActivities(): ArrayList<Activity> {
        val result = arrayListOf<Activity>()
        this.forEach {
            result.add(Activity(it.id.toLong(), it.idUser.toLong(), it.amout, it.date, it.time))
        }
        return result
    }

    fun List<WaterEntity>.toActivityList(): ArrayList<Activity> {
        val result = arrayListOf<Activity>()
        this.forEach {
            result.add(Activity(it.id, it.idUser, it.amount, it.date, it.time))
        }
        return result
    }

    fun Activity.toWater(): WaterEntity {
        return WaterEntity(id!!, idUser, date, amount, time)
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

    fun User.toUserEntity(): UserEntity {
        var idActivity = 0
        ActivityType.entries.forEachIndexed { index, it ->
            if (it == this.activityType)
                idActivity = index
        }

        val gender = if (this.gender == "Male") 1 else 0

        return UserEntity(
            this.id, idActivity.toLong(), this.idLeague,
            this.email, this.password,
            this.joinDate, this.name,
            this.birthDate, this.weight,
            this.height, gender,
            this.score, this.target, this.profile
        )
    }

    fun GetUser.User.toUser(): User {

        val activity = ActivityType.entries[this.idActivity - 1]
        val age = DateUtils.calculateAge(this.birthdate)
        val genderText = if (this.gender == 1) "Male" else "Female"

        return User(
            this.id.toLong(),
            activity,
            this.idleague.toLong(),
            this.email,
            this.password,
            this.name,
            this.date,
            this.target,
            age.toString(),
            this.birthdate,
            this.weight.toString(),
            this.height.toString(),
            genderText,
            this.score,
            this.profile
        )
    }

    fun GetCups.toCups(): List<Cup> {
        val cups = this.data!!
        val result = arrayListOf<Cup>()
        cups.forEach {
            result.add(
                Cup(
                    it.id.toLong(),
                    it.idUser.toLong(),
                    it.name,
                    it.capacity.toInt(),
                    it.color
                )
            )
        }
        return result
    }

    fun List<GlassEntity>.toCupList(): ArrayList<Cup> {
        val result = arrayListOf<Cup>()
        this.forEach {
            result.add(Cup(it.id, it.idUser, it.name, it.capacity.toInt(), it.color))
        }
        return result
    }

    fun Cup.toGlass(): GlassEntity {
        return GlassEntity(id!!, idUser, title, capacity.toString(), color)
    }

    fun Double.toLiter(): Double {
        return this / 1000.000
    }

    fun Double.toMilliLiter(): Double {
        return this * 1000
    }
}