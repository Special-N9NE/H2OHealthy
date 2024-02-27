package org.n9ne.auth.repo

import org.n9ne.common.model.CompleteProfileResult
import org.n9ne.common.model.LoginResult
import org.n9ne.common.source.objects.Auth
import org.n9ne.common.util.RepoCallback

interface AuthRepo {
    suspend fun login(email: String, password: String, callback: RepoCallback<LoginResult>)

    suspend fun register(
        name: String,
        email: String,
        password: String,
        callback: RepoCallback<Unit>
    )

    suspend fun completeProfile(
        data: Auth.CompleteProfile,
        callback: RepoCallback<CompleteProfileResult>
    )

    suspend fun sendRecovery(
        email: String,
        name: String,
        text: String,
        callback: RepoCallback<String>
    )
}