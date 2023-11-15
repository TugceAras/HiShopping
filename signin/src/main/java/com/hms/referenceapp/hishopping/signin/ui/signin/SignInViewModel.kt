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
package com.hms.referenceapp.hishopping.signin.ui.signin

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.account.AccountService
import com.hms.lib.commonmobileservices.account.SignInUser
import com.hms.lib.commonmobileservices.auth.AuthService
import com.hms.lib.commonmobileservices.auth.AuthUser
import com.hms.lib.commonmobileservices.core.ResultCallback
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.signin.domain.IsUserExistUseCase
import com.hms.referenceapp.hishopping.signin.domain.UpsertUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService,
    private val authService: AuthService,
    private val isUserExistUseCase: IsUserExistUseCase,
    private val upsertUserUseCase: UpsertUserUseCase
) : BaseViewModel() {

    fun getSignInIntent(intent: (Intent) -> Unit) {
        accountService.getSignInIntent(intent)
    }

    fun huaweiGoogleSignInResult(intent: Intent, token: (String?) -> Unit) {
        accountService.onSignInActivityResult(intent, object : ResultCallback<SignInUser> {
            override fun onSuccess(result: SignInUser?) {
                token.invoke(result?.authServiceToken)
            }

            override fun onFailure(error: Exception) {
                token.invoke(null)
            }

            override fun onCancelled() {
                token.invoke(null)
            }

        })
    }

    private val _signedUser = MutableLiveData<ResultData<AuthUser>>()
    val signedUser: LiveData<ResultData<AuthUser>>
        get() = _signedUser

    fun signInWithHuaweiGoogle(token: String) {
        _signedUser.postValue(ResultData.Loading())
        authService.signInWithGoogleOrHuawei(token)
            .addOnSuccessListener { checkUser(it) }
            .addOnFailureListener { _signedUser.postValue(ResultData.Failed(it.message)) }
    }

    fun signInWithFacebook(token: String) {
        _signedUser.postValue(ResultData.Loading())
        authService.signInWithFacebook(token)
            .addOnSuccessListener { _signedUser.postValue(ResultData.Success(it)) }
            .addOnFailureListener { _signedUser.postValue(ResultData.Failed(it.message)) }
    }

    private fun checkUser(user:AuthUser) = viewModelScope.launch {
        isUserExistUseCase.execute(user.id).collect{
            when(it){
                is ResultData.Success -> {
                    it.data?.let { isUserExist ->
                        if (isUserExist) _signedUser.postValue(ResultData.Success(user))
                        else addUserToCloudDB(user)
                    }?: run{_signedUser.postValue(ResultData.Success(user))}
                }
                is ResultData.Failed -> _signedUser.postValue(ResultData.Success(user))
                is ResultData.Loading -> {}
            }
        }
    }

    private fun addUserToCloudDB(authUser:AuthUser) = viewModelScope.launch{
        upsertUserUseCase.execute(
            User(
            id = authUser.id,
            fullName = authUser.displayName,
            email = authUser.email,
            photoUrl = authUser.photoUrl,
            serviceType = authUser.serviceType.value,
            providerType = authUser.providerType.value
        )).collect{
            when(it){
                is ResultData.Success -> _signedUser.postValue(ResultData.Success(authUser))
                is ResultData.Failed -> _signedUser.postValue(ResultData.Success(authUser))
                is ResultData.Loading -> {}
            }
        }
    }
}