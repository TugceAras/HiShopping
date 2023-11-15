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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.makeGone
import com.hms.referenceapp.hishopping.base.extensions.makeVisible
import com.hms.referenceapp.hishopping.core.Device
import com.hms.referenceapp.hishopping.core.MobileServiceType
import com.hms.referenceapp.hishopping.signin.R
import com.hms.referenceapp.hishopping.signin.databinding.FragmentSignInBinding
import com.hms.referenceapp.hishopping.signin.ui.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<SignInViewModel, FragmentSignInBinding>() {
    override val layoutRes: Int = R.layout.fragment_sign_in

    private var facebookCallbackManager: CallbackManager? = CallbackManager.Factory.create()

    companion object {
        private const val HUAWEI_GOOGLE_SIGN_IN = 1001
        private const val TAG = "SignInFragment"
    }

    override fun observeViewModel() {
        viewModel.signedUser.observe(viewLifecycleOwner) {
            when (it) {
                is ResultData.Loading -> {
                    Log.i(TAG, "signedUser loading")
                }
                is ResultData.Success -> {
                    Log.i(TAG, "signedUser success")
                    (activity as SignInActivity?)?.onConnected()
                }
                is ResultData.Failed -> {
                    Log.e(TAG, "signedUser error -> ${it.error}")
                    (activity as SignInActivity?)?.onError(it.error!!)
                }
            }
        }
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        arrangeButtons()
        initFacebookLogin()
        binding.imageViewSignInEmail.setOnClickListener { findNavController().navigate(R.id.action_signInFragment_to_signInWithEmailFragment) }
        binding.buttonSignInSignUp.setOnClickListener { findNavController().navigate(R.id.action_signInFragment_to_signUpFragment) }
        binding.imageViewSignInHuawei.setOnClickListener {
            viewModel.getSignInIntent {
                startActivityForResult(it, HUAWEI_GOOGLE_SIGN_IN)
            }
        }
        binding.imageViewSignInGoogle.setOnClickListener {
            viewModel.getSignInIntent {
                startActivityForResult(it, HUAWEI_GOOGLE_SIGN_IN)
            }
        }
        binding.imageViewSignInFacebook.setOnClickListener {
            binding.buttonSignInFacebook.performClick()
        }
    }

    private fun arrangeButtons() {
        if (Device.getMobileServiceType(requireContext()) == MobileServiceType.GMS) {
            binding.imageViewSignInGoogle.makeVisible()
            binding.imageViewSignInHuawei.makeGone()
        }
        if (Device.getMobileServiceType(requireContext()) == MobileServiceType.HMS) {
            binding.imageViewSignInGoogle.makeGone()
            binding.imageViewSignInHuawei.makeVisible()
        }
    }

    private fun initFacebookLogin() {
        binding.buttonSignInFacebook.setPermissions("email")
        binding.buttonSignInFacebook.registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    result?.accessToken?.let {
                        viewModel.signInWithFacebook(it.token)
                    }
                }

                override fun onCancel() {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.error_sign_in_facebook_cancel),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.error_sign_in_facebook_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookCallbackManager?.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == HUAWEI_GOOGLE_SIGN_IN) {
                viewModel.huaweiGoogleSignInResult(data!!) {
                    it?.let {
                        viewModel.signInWithHuaweiGoogle(it)
                    }
                }
            }
        }
    }
}