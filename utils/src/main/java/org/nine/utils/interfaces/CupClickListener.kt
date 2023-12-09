package org.n9ne.h2ohealthy.util.interfaces

import org.n9ne.h2ohealthy.data.model.Cup

interface CupClickListener {
    fun onClick(item: Cup)
}