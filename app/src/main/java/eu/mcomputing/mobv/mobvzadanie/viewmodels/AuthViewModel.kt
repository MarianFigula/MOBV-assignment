package eu.mcomputing.mobv.mobvzadanie.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.mcomputing.mobv.mobvzadanie.data.DataRepository
import eu.mcomputing.mobv.mobvzadanie.data.model.User
import kotlinx.coroutines.launch

class AuthViewModel(private val dataRepository: DataRepository) : ViewModel() {
    private val TAG = "AuthViewModel"
    private val _registrationResult = MutableLiveData<String>()
    val registrationResult: LiveData<String> get() = _registrationResult

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> get() = _loginResult

    private val _userResult = MutableLiveData<User?>()
    val userResult: LiveData<User?> get() = _userResult


    private val _resetPasswordResult = MutableLiveData<Pair<String, String?>>()
    private val _changePasswordResult = MutableLiveData<String>()

    val resetPasswordResult : LiveData<Pair<String, String?>> get() = _resetPasswordResult
    val changePasswordResult : LiveData<String> get() = _changePasswordResult

    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()

    val changePasswordOld = MutableLiveData<String>()
    val changePasswordNew = MutableLiveData<String>()

    fun registerUser() {
        viewModelScope.launch {
            val result = dataRepository.apiRegisterUser(
                username.value ?: "",
                email.value ?: "",
                password.value ?: "",
                repeatPassword.value ?: ""
            )
            _registrationResult.postValue(result.first ?: "")
            _userResult.postValue(result.second)
        }
    }

    fun loginUser() {
        viewModelScope.launch {
            val result = dataRepository.apiLoginUser(email.value ?: "", password.value ?: "")
            Log.d(TAG, result.toString())
            _loginResult.postValue(result.first ?: "")
            _userResult.postValue(result.second)
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

    fun changePassword(){
        viewModelScope.launch {
            val result = dataRepository.apiChangePassword(
                changePasswordOld.value ?: "",
                changePasswordNew.value ?: "")
            _changePasswordResult.postValue(result.first)
        }
    }

}