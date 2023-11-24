package org.n9ne.h2ohealthy.data.repo

import org.n9ne.h2ohealthy.data.model.Cup
import org.n9ne.h2ohealthy.data.model.User
import org.n9ne.h2ohealthy.util.RepoCallback

interface ProfileRepo {
    fun getUser(callback: RepoCallback<User>)
    fun getCups(callback: RepoCallback<List<Cup>>)
    fun addCup(cup: Cup, callback: RepoCallback<Long>)
    fun updateCup(cup: Cup, callback: RepoCallback<Unit>)
    fun removeCup(cup: Cup, callback: RepoCallback<Unit>)
}