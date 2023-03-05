package ru.gas.humblr.presentation

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.openid.appauth.AuthorizationService
import ru.gas.humblr.data.remote.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository,
) : AndroidViewModel(application) {

    private val authService: AuthorizationService = AuthorizationService(getApplication())
    private val _isAuthSucess = MutableStateFlow(false)
    val isAuthSuccess = _isAuthSucess.asStateFlow()

    fun startAuthIntent(): Intent {
        return authRepository.sendAuthRequest(authService)
    }

    fun requestToken(code: String) {
        _isAuthSucess.value = authRepository.performRequestToken(code, authService)
    }


    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}