package org.n9ne.h2ohealthy.data.repo

import org.n9ne.h2ohealthy.data.source.objects.Auth
import org.n9ne.h2ohealthy.util.RepoCallback

interface AuthRepo {
    suspend fun login(email: String, password: String, callback: RepoCallback<String>)

    suspend fun register(
        name: String,
        email: String,
        password: String,
        callback: RepoCallback<Unit>
    )

    suspend fun completeProfile(
        data: Auth.CompleteProfile,
        callback: RepoCallback<String>
    )
}