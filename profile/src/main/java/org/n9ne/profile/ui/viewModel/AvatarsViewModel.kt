package org.n9ne.profile.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.n9ne.common.BaseViewModel
import org.n9ne.common.model.Image
import org.n9ne.common.util.Event
import org.n9ne.common.util.RepoCallback
import org.n9ne.profile.R
import org.n9ne.profile.repo.ProfileRepo
import javax.inject.Inject

@HiltViewModel
class AvatarsViewModel @Inject constructor() : BaseViewModel<ProfileRepo>() {

    val ldSuccess = MutableLiveData<Event<Unit>>()

    fun saveProfile(token: String?, image: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo?.updateProfile(token!!, image, object : RepoCallback<String> {
                override fun onSuccessful(response: String) {

                    syncProfile(image)
                }

                override fun onError(error: String, isNetwork: Boolean, isToken: Boolean) {
                    ldError.postValue(Event(error))
                }
            })
        }
    }


    private fun syncProfile(image: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    db?.roomDao()?.updateProfile(image)

                    ldSuccess.postValue(Event(Unit))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    val avatars = listOf(
        Image("avatar01" , R.drawable.avatar01),
        Image("avatar02" , R.drawable.avatar02),
        Image("avatar03" , R.drawable.avatar03),
        Image("avatar04" , R.drawable.avatar04),
        Image("avatar05" , R.drawable.avatar05),
        Image("avatar06" , R.drawable.avatar06),
        Image("avatar07" , R.drawable.avatar07),
        Image("avatar08" , R.drawable.avatar08),
        Image("avatar09" , R.drawable.avatar09),
        Image("avatar10" , R.drawable.avatar10),
        Image("avatar11" , R.drawable.avatar11),
        Image("avatar12" , R.drawable.avatar12),
        Image("avatar13" , R.drawable.avatar13),
        Image("avatar14" , R.drawable.avatar14),
        Image("avatar15" , R.drawable.avatar15),
        Image("avatar16" , R.drawable.avatar16),
        Image("avatar17" , R.drawable.avatar17),
        Image("avatar18" , R.drawable.avatar18),
        Image("avatar19" , R.drawable.avatar19),
        Image("avatar20" , R.drawable.avatar20),
        Image("avatar21" , R.drawable.avatar21),
        Image("avatar22" , R.drawable.avatar22),
        Image("avatar23" , R.drawable.avatar23),
        Image("avatar24" , R.drawable.avatar24),
        Image("avatar25" , R.drawable.avatar25),
        Image("avatar26" , R.drawable.avatar26),
        Image("avatar27" , R.drawable.avatar27),
        Image("avatar28" , R.drawable.avatar28),
        Image("avatar29" , R.drawable.avatar29),
        Image("avatar30" , R.drawable.avatar30),
        Image("avatar31" , R.drawable.avatar31),
        Image("avatar32" , R.drawable.avatar32)
    )
}