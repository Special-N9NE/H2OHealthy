package org.n9ne.common.util.interfaces

import org.n9ne.common.model.Cup

interface CupEditListener {
    fun onEdit(cup: Cup, edit: Boolean)
}