package me.auth_android.auth_sample_2.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.auth_android.auth_kit.data.auth.defines.Authenticating

@HiltViewModel
class AppViewModel @Inject constructor(private val authReposiatory: Authenticating) : ViewModel() {
    private val _user = MutableStateFlow("unknown")
    val user = _user.asStateFlow()

    init {
        _user.value = authReposiatory.currentUser.let { (it?.userID ?: "unknown").toString() }
    }
}
