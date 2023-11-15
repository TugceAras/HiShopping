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
package com.hms.referenceapp.hishopping.app.profileedit.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.auth.AuthService
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.profile.domain.GetUserUseCase
import com.hms.referenceapp.hishopping.app.profileedit.domain.UpdateUserUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.core.UIText
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.signin.util.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val authService: AuthService,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase
): BaseViewModel() {

    private val _uiState = MutableLiveData<ProfileEditViewState>()
    val uiState:LiveData<ProfileEditViewState> get() = _uiState

    fun changeEmail(userId: String?,email:String?) = viewModelScope.launch{
        _uiState.value = ProfileEditViewState(loading = true, noMessage = true)
        userId?.let { id ->
            email?.let { userEmail->
                if (isEmailValid(userEmail)){
                    updateUser(id){
                        this.launch { updateCloudDBUser(it.copy(email = userEmail)) }
                    }
                }else ProfileEditViewState(errorMessage = UIText.ResourceText(R.string.error_wrong_email_format_message))
            }?: {_uiState.value = ProfileEditViewState(errorMessage = UIText.ResourceText(R.string.profile_edit_change_email_empty_message)) }
        }?:run { _uiState.value = ProfileEditViewState(errorMessage = UIText.ResourceText(R.string.unexpected_error)) }
    }

    fun changePassword(password:String?,verifyCode:String?){
        _uiState.value = ProfileEditViewState(loading = true, noMessage = true)
        if (!password.isNullOrEmpty() && !verifyCode.isNullOrEmpty()){
            authService.updatePasswordwEmail(password,verifyCode)
            _uiState.value = ProfileEditViewState(
                successMessage = UIText.ResourceText(R.string.profile_edit_password_change_message)
            )
        }else{
            _uiState.value = ProfileEditViewState(
                errorMessage = UIText.ResourceText(R.string.profile_edit_change_password_empty_message)
            )
        }
    }

    fun getCodeForPassword(email: String?){
        _uiState.value = ProfileEditViewState(loading = true, noMessage = true)
        if (!email.isNullOrEmpty()){
            authService.getCodePassword(email)
            _uiState.value = ProfileEditViewState(successMessage = UIText.ResourceText(R.string.profile_edit_code_sent_message))
        }else {
            _uiState.value = ProfileEditViewState(
                errorMessage = UIText.ResourceText(R.string.profile_edit_email_empty_message)
            )
        }
    }

    fun updateFullName(userId: String,fullName:String?) = viewModelScope.launch {
        _uiState.value = ProfileEditViewState(loading = true, noMessage = true)
        fullName?.let { name ->
            updateUser(userId){
                authService.updateUsername(fullName)
                this.launch { updateCloudDBUser(it.copy(fullName = name)) }
            }
        }?:run {ProfileEditViewState(errorMessage = UIText.ResourceText(R.string.profile_edit_full_name_empty_message))}
    }

    private suspend fun updateUser(userId:String,update:(user:User)->Unit){
        getUserUseCase.execute(userId).collect{ cloudResult ->
            when(cloudResult){
                is ResultData.Success -> {
                    cloudResult.data?.let { userFromCloud ->
                        update(userFromCloud)
                    }?: run { _uiState.value = ProfileEditViewState(
                        errorMessage = UIText.ResourceText(R.string.unexpected_error))
                    }
                }
                is ResultData.Failed -> {
                    cloudResult.error?.let {
                        ProfileEditViewState(errorMessage = UIText.StringText(it))
                    }?:run { ProfileEditViewState(errorMessage = UIText.ResourceText(R.string.unexpected_error)) }
                }
            }
        }
    }

    private suspend fun updateCloudDBUser(user: User){
        updateUserUseCase.execute(user).collect{ updateResult ->
            when(updateResult){
                is ResultData.Success -> {
                    _uiState.value = ProfileEditViewState(
                        successMessage = UIText.ResourceText(R.string.profile_edit_change_user_info_message)
                    )
                }
                is ResultData.Failed -> {
                    updateResult.error?.let {
                        ProfileEditViewState(errorMessage = UIText.StringText(it))
                    }?:run { ProfileEditViewState(errorMessage = UIText.ResourceText(R.string.unexpected_error)) }
                }
            }
        }
    }
}