package org.n9ne.h2ohealthy.util.interfaces

import org.n9ne.h2ohealthy.data.model.Setting

interface SettingClickListener {
    fun settingClicked(setting: Setting)
}