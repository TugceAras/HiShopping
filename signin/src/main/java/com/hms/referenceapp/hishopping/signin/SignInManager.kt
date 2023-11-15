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
package com.hms.referenceapp.hishopping.signin

import android.app.Activity
import android.content.Intent
import com.hms.lib.commonmobileservices.account.AccountService
import com.hms.lib.commonmobileservices.account.SignInParams
import com.hms.lib.commonmobileservices.auth.AuthService
import com.hms.lib.commonmobileservices.auth.AuthUser
import com.hms.lib.commonmobileservices.auth.common.ProviderType
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.signin.ui.SignInActivity

class SignInManager(activity: Activity) {

    private val authService: AuthService = AuthService.Factory.create(activity)
    private val accountService: AccountService = AccountService.Factory.create(
        activity,
        SignInParams.Builder()
            .requestEmail()
            .requestAccessToken()
            .requestIdToken(activity.getString(R.string.google_client_id))
            .create()
    )

    fun getUser(): User? {
        val authUser = authService.getUser()
        return if (authUser == null) {
            null
        } else {
            User(
                id = authUser.id,
                fullName = authUser.displayName,
                email = authUser.email.ifEmpty { getEmail(authUser) },
                photoUrl = authUser.photoUrl,
                serviceType = authUser.serviceType.value,
                providerType = authUser.providerType.value
            )
        }
    }

    fun getUserByFullName(fullName: String): User? {
        val authUser = authService.getUser()
        return if (authUser == null) {
            null
        } else {
            User(
                id = authUser.id,
                fullName = fullName,
                email = authUser.email.ifEmpty { getEmail(authUser) },
                photoUrl = authUser.photoUrl,
                serviceType = authUser.serviceType.value,
                providerType = authUser.providerType.value
            )
        }
    }

    private fun getEmail(authUser: AuthUser): String {
        return if (authUser.providerType == ProviderType.Google || authUser.providerType == ProviderType.Huawei) {
            accountService.getEmail() ?: ""
        } else {
            ""
        }
    }

    fun signOut() {
        authService.signOut()
    }

    fun getIntent(activity: Activity, intent: (Intent) -> Unit) {
        intent.invoke(Intent(activity, SignInActivity::class.java))
    }
}