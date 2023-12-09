package org.n9ne.h2ohealthy.util.interfaces

import org.n9ne.h2ohealthy.data.model.Cup

interface CupEditListener {
    fun onEdit(cup: Cup, edit: Boolean)
}