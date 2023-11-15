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
package com.hms.referenceapp.hishopping.signin.di

import android.content.Context
import com.hms.lib.commonmobileservices.account.AccountService
import com.hms.lib.commonmobileservices.account.SignInParams
import com.hms.lib.commonmobileservices.auth.AuthService
import com.hms.referenceapp.hishopping.signin.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/*val mServiceModule = module {
    single {
        AccountService.Factory.create(
            androidContext(),
            SignInParams.Builder()
                .requestEmail()
                .requestAccessToken()
                .requestIdToken(androidContext().getString(R.string.google_client_id))
                .create()
        )
    } bind AccountService::class
    single { AuthService.Factory.create(androidContext()) } bind AuthService::class
}*/

@Module
@InstallIn(ActivityRetainedComponent::class)
object SignInModule {

    @Provides
    fun provideAccountService(@ApplicationContext context: Context) = AccountService.Factory.create(
        context,
        SignInParams.Builder()
            .requestEmail()
            .requestAccessToken()
            .requestIdToken(context.getString(R.string.google_client_id))
            .create()
    )

    @Provides
    fun provideAuthService(@ApplicationContext context: Context) =  AuthService.Factory.create(context)

}
