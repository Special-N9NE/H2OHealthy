package org.n9ne.common.util

import org.n9ne.common.model.Activity
import org.n9ne.common.model.ActivityType
import org.n9ne.common.model.Cup
import org.n9ne.common.model.League
import org.n9ne.common.model.Member
import org.n9ne.common.model.User
import org.n9ne.common.source.local.GlassEntity
import org.n9ne.common.source.local.LeagueEntity
import org.n9ne.common.source.local.UserEntity
import org.n9ne.common.source.local.WaterEntity
import org.n9ne.common.source.objects.GetCups
import org.n9ne.common.source.objects.GetMembers
import org.n9ne.common.source.objects.GetProgress
import org.n9ne.common.source.objects.GetUser

object Mapper {
    fun List<GetProgress.Data>.toActivities(): ArrayList<Activity> {
        val result = arrayListOf<Activity>()
        forEach {
            result.add(
                Activity(
                    it.id.toLong(),
                    it.idUser.toLong(),
                    it.amout,
                    it.date,
                    it.time
                )
            )
        }
        return result
    }

    fun List<WaterEntity>.toActivityList(): ArrayList<Activity> {
        val result = arrayListOf<Activity>()
        forEach {
            result.add(
                Activity(
                    it.id,
                    it.idUser,
                    it.amount,
                    it.date,
                    it.time
                )
            )
        }
        return result
    }

    fun Activity.toWater(): WaterEntity {
        return WaterEntity(id!!, idUser, date, amount, time)
    }

    fun League.toLeagueEntity(): LeagueEntity {
        return LeagueEntity(
            id!!, idUser, name, code
        )
    }

    fun GetMembers.toMembers(): List<Member> {

        val result = ArrayList<Member>()
        members!!.forEach {
            result.add(
                Member(it.id, it.name, it.score, it.profile)
            )
        }
        return result
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
            score.toInt(),
            profile
        )
    }

    fun User.toUserEntity(): UserEntity {
        var idActivity = 0
        ActivityType.entries.forEachIndexed { index, it ->
            if (it == activityType)
                idActivity = index
        }

        val gender = if (this.gender == "Male") 1 else 0

        return UserEntity(
            id, idActivity.toLong(), idLeague,
            email, password,
            joinDate, name,
            birthDate, weight,
            height, gender,
            score.toString(), target, profile
        )
    }

    fun GetUser.User.toUser(): User {

        val activity = ActivityType.entries[idActivity - 1]
        val age = DateUtils.calculateAge(birthdate)
        val genderText = if (gender == 1) "Male" else "Female"

        return User(
            id.toLong(),
            activity,
            idleague.toLong(),
            email,
            password,
            name,
            date,
            target,
            age.toString(),
            birthdate,
            weight.toString(),
            height.toString(),
            genderText,
            score,
            profile
        )
    }

    fun GetCups.toCups(): List<Cup> {
        val cups = data!!
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
        forEach {
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