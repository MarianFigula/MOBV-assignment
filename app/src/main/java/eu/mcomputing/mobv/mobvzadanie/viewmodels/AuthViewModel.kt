package eu.mcomputing.mobv.mobvzadanie.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.mcomputing.mobv.mobvzadanie.DataRepository
import eu.mcomputing.mobv.mobvzadanie.data.model.User
import kotlinx.coroutines.launch

class AuthViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val _registrationResult = MutableLiveData<Pair<String, User?>>()
    private val _loginResult = MutableLiveData<Pair<String, User?>>()
    private val _resetPasswordResult = MutableLiveData<Pair<String, String?>>()
    private val _changePasswordResult = MutableLiveData<Pair<String, String?>>()

    val registrationResult : LiveData<Pair<String, User?>> get() = _registrationResult
    val loginResult : LiveData<Pair<String, User?>> get() = _loginResult
    val resetPasswordResult : LiveData<Pair<String, String?>> get() = _resetPasswordResult
    val changePasswordResult : LiveData<Pair<String, String?>> get() = _changePasswordResult

    fun registerUser(
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ){

        viewModelScope.launch {
            _registrationResult.postValue(dataRepository.
            apiRegisterUser(
                username,
                email,
                password,
                repeatPassword
            ))
        }
    }

    fun loginUser(
        email: String,
        password: String
    ){
        viewModelScope.launch {
            _loginResult.postValue(dataRepository.
            apiLoginUser(
                email,
                password
            ))
        }
    }

    fun resetPassword(
        email: String
    ){
        viewModelScope.launch {
            _resetPasswordResult.postValue(dataRepository
                .apiResetPassword(
                    email
                ))
        }
    }

    fun changePassword(
        authToken: String,
        oldPassword: String,
        newPassword: String
    ){
        viewModelScope.launch {
            _changePasswordResult.postValue(dataRepository.
            apiChangePassword(
                authToken,
                oldPassword,
                newPassword
            ))
        }
    }

}