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
package com.hms.referenceapp.hishopping.signin.ui.email.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hms.lib.commonmobileservices.auth.AuthService
import com.hms.lib.commonmobileservices.auth.AuthUser
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInWithEmailViewModel @Inject constructor(private val authService: AuthService) : BaseViewModel() {
    private val _user = MutableLiveData<ResultData<AuthUser>>()
    val user: LiveData<ResultData<AuthUser>>
        get() = _user

    fun signIn(email: String, password: String) {
        _user.postValue(ResultData.Loading())
        authService.signInWithEmail(email, password)
            .addOnSuccessListener { _user.postValue(ResultData.Success(it)) }
            .addOnFailureListener { _user.postValue(ResultData.Failed(it.message)) }
    }
}