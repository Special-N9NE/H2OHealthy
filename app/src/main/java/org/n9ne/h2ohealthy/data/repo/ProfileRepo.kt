package org.n9ne.h2ohealthy.data.repo

import org.n9ne.h2ohealthy.data.repo.local.UserEntity
import org.n9ne.h2ohealthy.util.RepoCallback

interface ProfileRepo {
    fun getUser(callback: RepoCallback<UserEntity>)
}