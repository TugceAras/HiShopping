// See the License for the specific language governing permissions and
// limitations under the License.
//
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.referenceapp.hishopping.signin.ui.email.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.auth.AuthService
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.signin.domain.UpsertUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyCodeToSignUpViewModel @Inject constructor(
    private val authService: AuthService,
    private val upsertUserUseCase: UpsertUserUseCase
) : BaseViewModel() {
    private val _codeVerificationResult = MutableLiveData<ResultData<Unit>>()
    val codeVerificationResult: LiveData<ResultData<Unit>>
        get() = _codeVerificationResult

    private val _upsertUserResult = MutableLiveData<ResultData<Unit>>()
    val upsertUserResult: LiveData<ResultData<Unit>> get() = _upsertUserResult

    fun verifyCode(email: String, password: String, verificationCode: String) {
        _codeVerificationResult.postValue(ResultData.Loading())
        authService.verifyCode(email, password, verificationCode)
            .addOnSuccessListener { _codeVerificationResult.postValue(ResultData.Success(it)) }
            .addOnFailureListener { _codeVerificationResult.postValue(ResultData.Failed(it.message)) }
    }

    fun upsertUserToCloudDB(user: User) = viewModelScope.launch {
        upsertUserUseCase.execute(user).collect{
            _upsertUserResult.value = it
        }
    }
}