package me.auth_kit_aos.auth_sample_2.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.auth_kit_aos.auth_kit.auth.defines.Authenticating

@HiltViewModel
class AppViewModel @Inject constructor(private val authReposiatory: Authenticating) : ViewModel() {
    private val _user = MutableStateFlow("unknown")
    val user = _user.asStateFlow()

    init {
        _user.value = authReposiatory.currentUser.let { (it?.userID ?: "unknown").toString() }
    }
}
