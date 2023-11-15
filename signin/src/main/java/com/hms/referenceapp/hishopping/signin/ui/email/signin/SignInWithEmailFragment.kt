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

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hms.lib.commonmobileservices.core.*
import com.hms.lib.commonmobileservices.safety.SafetyService
import com.hms.lib.commonmobileservices.safety.SafetyServiceResponse
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.Device
import com.hms.referenceapp.hishopping.base.extensions.MobileServiceType
import com.hms.referenceapp.hishopping.base.extensions.hideKeyboard
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_DEVICE_BLOCKED
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_INVALID_PASSWORD
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_NO_EXISTING_USER
import com.hms.referenceapp.hishopping.signin.databinding.FragmentSignInWithEmailBinding
import com.hms.referenceapp.hishopping.signin.ui.SignInActivity
import com.hms.referenceapp.hishopping.signin.util.isEmailValid
import com.hms.referenceapp.hishopping.signin.util.isPasswordValid
import com.hms.referenceapp.hishopping.signin.R
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class SignInWithEmailFragment :
    BaseFragment<SignInWithEmailViewModel, FragmentSignInWithEmailBinding>() {
    override val layoutRes: Int = R.layout.fragment_sign_in_with_email
    private val args: SignInWithEmailFragmentArgs by navArgs()

    private var safetyService: SafetyService? = null
    private var appKey: String = ""

    companion object {
        private const val TAG = "SignInWithEmailFragment"
    }

    override fun observeViewModel() {
        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is ResultData.Loading -> {
                    Log.i(TAG, "user loading")

                }
                is ResultData.Success -> {
                    Log.i(TAG, "user success")
                    (activity as SignInActivity?)?.onConnected()
                }
                is ResultData.Failed -> {
                    Log.e(TAG, "user error -> ${it.error}")
                    when {

                        it.error!!.contains(AUTH_SERVICE_ERROR_NO_EXISTING_USER) -> {
                            binding.textViewSignInWithEmailFeedback.text =
                                getString(R.string.error_sign_in_with_email_user_not_registered_message)
                        }
                        it.error!!.contains(AUTH_SERVICE_ERROR_INVALID_PASSWORD) -> {
                            binding.textViewSignInWithEmailFeedback.text =
                                getString(R.string.error_sign_in_with_email_password_email_mismatch_message)
                        }
                        it.error!!.contains(AUTH_SERVICE_ERROR_DEVICE_BLOCKED) -> {
                            binding.textViewSignInWithEmailFeedback.text =
                                getString(R.string.error_sign_in_with_email_interval_limit_message)
                        }
                        else -> {
                            (activity as SignInActivity?)?.onError(it.error!!)
                        }
                    }

                }
            }
        }
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        if (!args.email.isNullOrBlank() && !args.password.isNullOrBlank()) {
            args.email?.let { email ->
                args.password?.let { password ->
                    viewModel.signIn(email, password)
                }
            }
        }

        binding.buttonSignInWithEmailChangeSignInMethod.setOnClickListener { findNavController().navigateUp() }
        binding.buttonSignInWithEmailResetPassword.setOnClickListener {
            findNavController().navigate(
                R.id.action_signInWithEmailFragment_to_resetPasswordFragment
            )
        }
        binding.buttonSignInWithEmail.setOnClickListener { signIn() }

        safetyService = SafetyService.Factory.create(requireContext())
        appKey = if (Device.getMobileServiceType(requireContext()) == MobileServiceType.GMS) {
            requireContext().getString(R.string.google_site_api_key)
        } else {
            requireContext().getString(R.string.app_id)
        }
    }

    private fun signIn() {
        this.hideKeyboard()
        val email = binding.editTextSignInWithEmail.text.toString()
        val password = binding.editTextSignInEmailPassword.text.toString()

        if (isEmailValid(email)) {
            if (isPasswordValid(password)) {
                safetyService?.userDetect(
                    appKey,
                    object : SafetyService.SafetyServiceCallback<SafetyServiceResponse> {
                        override fun onFailUserDetect(e: Exception) {
                            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onSuccessUserDetect(result: SafetyServiceResponse?) {
                            viewModel.signIn(email, password)
                        }
                    })
            } else {
                binding.textViewSignInWithEmailFeedback.text =
                    getString(R.string.error_email_wrong_password_format_message)
            }
        } else {
            binding.textViewSignInWithEmailFeedback.text =
                getString(R.string.error_wrong_email_format_message)
        }
    }
}