package com.ucb.ucbtest.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.data.LoginRepository
import com.ucb.framework.datastore.LoginDataSource
import com.ucb.usecases.DoLogin
import com.ucb.usecases.ObtainToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginUseCase: DoLogin,
    val obtainToken: ObtainToken
): ViewModel() {

    sealed class LoginState {
        object Init: LoginState()
        object Loading: LoginState()
        class Successful: LoginState()
        class Error(val message: String): LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Init)
    var loginState : StateFlow<LoginState> = _loginState


    fun doLogin(userName: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result: Result<Unit> = loginUseCase.invoke(userName = userName, password = password)

            when {
                result.isSuccess -> {
                    _loginState.value = LoginState.Successful()
                    obtainToken.getToken()
                }
                result.isFailure -> {
                    _loginState.value = LoginState.Error(message = "Invalid credentials")
                }
            }
        }
    }
}