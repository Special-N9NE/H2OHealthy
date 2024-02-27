package org.n9ne.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.n9ne.common.source.local.AppDatabase
import org.n9ne.common.util.Event
import org.n9ne.common.util.interfaces.Navigator

abstract class BaseViewModel<T> : ViewModel() {

    var navigator: Navigator? = null

    var db: AppDatabase? = null

    var repo: T? = null

    val ldError = MutableLiveData<Event<String>>()
    val ldToken = MutableLiveData<Event<Unit>>()
    val ldLoading = MutableLiveData<Event<Boolean>>()
}