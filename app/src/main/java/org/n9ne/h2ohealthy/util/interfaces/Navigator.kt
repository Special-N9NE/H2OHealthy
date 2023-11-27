package org.n9ne.h2ohealthy.util.interfaces

import android.os.Bundle
import androidx.annotation.IdRes

interface Navigator {
    fun shouldNavigate(@IdRes destination: Int, data: Bundle? = null)
}