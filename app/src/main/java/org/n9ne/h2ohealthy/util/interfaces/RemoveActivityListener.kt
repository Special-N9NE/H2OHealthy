package org.n9ne.h2ohealthy.util.interfaces

import org.n9ne.h2ohealthy.data.model.Activity

interface RemoveActivityListener {
    fun onRemove(activity: Activity)
}