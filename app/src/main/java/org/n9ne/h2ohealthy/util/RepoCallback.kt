package org.n9ne.h2ohealthy.util

interface RepoCallback<T> {
    fun onSuccessful(response: T)
    fun onError(error: String, isNetwork: Boolean = false, isToken: Boolean = false)
}