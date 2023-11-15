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
package com.hms.referenceapp.hishopping.app.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.app.profile.domain.GetUserUseCase
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
): BaseViewModel() {

    private val _uiState = MutableLiveData<ProfileViewState>()
    val uiState: LiveData<ProfileViewState> get() = _uiState

    fun getUser(user:User) = viewModelScope.launch {
        getUserUseCase.execute(user.id).collect{ cloudResult ->
            when(cloudResult){
                is ResultData.Success -> {
                    cloudResult.data?.let { userFromCloud ->
                        _uiState.value = ProfileViewState(userFromCloud)
                        Log.d("GetUser","User From Cloud")
                    }?: run { ProfileViewState(user) }
                }
                is ResultData.Failed -> _uiState.value = ProfileViewState(user)
                is ResultData.Loading -> _uiState.value = ProfileViewState(user,loading = true)
            }
        }
    }
}