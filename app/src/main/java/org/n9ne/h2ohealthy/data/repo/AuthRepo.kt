package org.n9ne.h2ohealthy.data.repo

import org.n9ne.h2ohealthy.util.RepoCallback

interface AuthRepo {
    suspend fun login(email: String, password: String, callback: RepoCallback<String>)
}