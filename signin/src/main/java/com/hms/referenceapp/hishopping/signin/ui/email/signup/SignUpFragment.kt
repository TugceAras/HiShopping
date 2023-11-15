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

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hms.lib.commonmobileservices.auth.common.VerificationType
import com.hms.lib.commonmobileservices.core.Device
import com.hms.lib.commonmobileservices.core.MobileServiceType
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.safety.SafetyService
import com.hms.lib.commonmobileservices.safety.SafetyServiceResponse
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.hideKeyboard
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_EMAIL_ALREADY_USED
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_TOO_MANY_ATTEMPTS
import com.hms.referenceapp.hishopping.signin.R
import com.hms.referenceapp.hishopping.signin.databinding.FragmentSignUpBinding
import com.hms.referenceapp.hishopping.signin.util.isEmailValid
import com.hms.referenceapp.hishopping.signin.util.isFullNameValid
import com.hms.referenceapp.hishopping.signin.util.isPasswordValid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<SignUpViewModel, FragmentSignUpBinding>() {
    override val layoutRes: Int = R.layout.fragment_sign_up

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String
    private lateinit var fullName: String
    private var safetyService: SafetyService? = null
    private var appKey: String = ""

    companion object {
        private const val TAG = "SignUpFragment"
        private var observe = true
    }

    override fun observeViewModel() {
        viewModel.signUpResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Loading -> {
                    Log.i(TAG, "observeViewModel: loading")
                }
                is ResultData.Success -> {
                    if (observe) {
                        Log.i(TAG, "observeViewModel: success")
                        if (it.data == VerificationType.NON) {
                            showSuccessDialog(
                                "",
                                getString(R.string.sign_in),
                                getString(R.string.sign_up_success_dialog_message)
                            ) {
                                findNavController().navigate(R.id.action_signUpFragment_to_signInWithEmailFragment)
                            }
                        }
                        if (it.data == VerificationType.CODE) {
                            val action =
                                SignUpFragmentDirections
                                    .actionSignUpFragmentToVerifyCodeToSignUpFragment(
                                        email,
                                        password,
                                        fullName
                                    )
                            findNavController().navigate(action)
                        }
                        observe = false
                    }

                }
                is ResultData.Failed -> {
                    Log.i(TAG, "observeViewModel: failed -> ${it.error}")
                    if (it.error!!.contains(AUTH_SERVICE_ERROR_EMAIL_ALREADY_USED)) {
                        binding.textViewSignUpFeedback.text =
                            getString(R.string.error_user_has_been_registered_message)
                    }
                    if (it.error!!.contains(AUTH_SERVICE_ERROR_TOO_MANY_ATTEMPTS)) {
                        binding.textViewSignUpFeedback.text =
                            getString(R.string.error_verify_code_interval_limit_message)
                    }
                }
            }
        })
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSignUpSignIn.setOnClickListener { findNavController().navigateUp() }
        binding.buttonSignUp.setOnClickListener { signUp() }

        safetyService = SafetyService.Factory.create(requireContext())
        appKey = if (Device.getMobileServiceType(requireContext()) == MobileServiceType.GMS) {
            requireContext().getString(R.string.google_site_api_key)
        } else {
            requireContext().getString(R.string.app_id)
        }
    }

    private fun signUp() {
        this.hideKeyboard()
        observe = true
        email = binding.editTextSignUpEmail.text.toString()
        password = binding.editTextSignUpPassword.text.toString()
        passwordConfirm = binding.editTextSignUpPasswordConfirm.text.toString()
        fullName = binding.editTextSignUpFullName.text.toString().trim()

        if (isFullNameValid(fullName)){
            if (isEmailValid(email)) {
                if (isPasswordValid(password)) {
                    if (password == passwordConfirm) {
                        safetyService?.userDetect(
                            appKey,
                            object : SafetyService.SafetyServiceCallback<SafetyServiceResponse> {
                                override fun onFailUserDetect(e: Exception) {
                                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }

                                override fun onSuccessUserDetect(result: SafetyServiceResponse?) {
                                    viewModel.signUp(email, password)
                                }
                            })
                    } else {
                        binding.textViewSignUpFeedback.text =
                            getString(R.string.error_passwords_mismatch_message)
                    }
                } else {
                    binding.textViewSignUpFeedback.text =
                        getString(R.string.error_email_wrong_password_format_message)
                }
            } else {
                binding.textViewSignUpFeedback.text = getString(R.string.error_wrong_email_format_message)
            }
        }else{
            binding.textViewSignUpFeedback.text = getString(R.string.error_invalid_full_name_message)
        }
    }
}