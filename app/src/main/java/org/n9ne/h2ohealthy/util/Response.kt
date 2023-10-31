package org.n9ne.h2ohealthy.util

class Response<T>(response: T) {
    var notHandled = true
    var response: T
        get() {
            notHandled = false
            return field
        }

    init {
        this.response = response
    }
}
