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
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_EMAIL_ALREADY_USED
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_INVALID_VERIFICATION_CODE
import com.hms.referenceapp.hishopping.signin.R
import com.hms.referenceapp.hishopping.signin.SignInManager
import com.hms.referenceapp.hishopping.signin.databinding.FragmentVerifyCodeToSignUpBinding
import com.hms.referenceapp.hishopping.signin.ui.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyCodeToSignUpFragment :
    BaseFragment<VerifyCodeToSignUpViewModel, FragmentVerifyCodeToSignUpBinding>() {

    override val layoutRes: Int = R.layout.fragment_verify_code_to_sign_up
    private lateinit var signInManager: SignInManager
    private val args: VerifyCodeToSignUpFragmentArgs by navArgs()

    companion object {
        private const val TAG = "VerifyCodeToSignUpFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.i(TAG, "handleOnBackPressed: ")
                    findNavController().navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }


    override fun observeViewModel() {
        viewModel.codeVerificationResult.observe(viewLifecycleOwner) {
            when (it) {
                is ResultData.Loading -> {
                    Log.i(TAG, "verify code result loading")
                }
                is ResultData.Success -> {
                    Log.i(TAG, "verify code result success")
                    signInManager.getUserByFullName(args.fullName)?.let { user ->
                        viewModel.upsertUserToCloudDB(user)
                    }

                }
                is ResultData.Failed -> {
                    Log.e(TAG, "verify code result error -> ${it.error}")
                    if (it.error!!.contains(AUTH_SERVICE_ERROR_EMAIL_ALREADY_USED)) {
                        binding.textViewVerifyCodeToSignUpFeedback.text =
                            getString(R.string.error_user_has_been_registered_message)
                    }
                    if (it.error!!.contains(AUTH_SERVICE_ERROR_INVALID_VERIFICATION_CODE)) {
                        binding.textViewVerifyCodeToSignUpFeedback.text =
                            getString(R.string.error_wrong_verification_code_message)
                    }
                }
            }
        }
        viewModel.upsertUserResult.observe(viewLifecycleOwner){
            when(it){
                is ResultData.Success -> (activity as SignInActivity?)?.onConnected()
                is ResultData.Failed -> (activity as SignInActivity?)?.onConnected()
                is ResultData.Loading -> {}
            }

        }
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        signInManager = SignInManager(requireActivity())
        val email = args.email
        val password = args.password

        binding.editTextVerificationCodeToSignUp.transformationMethod = null
        binding.buttonVerifyCodeToSignUpOk.setOnClickListener {
            binding.editTextVerificationCodeToSignUp.text?.toString()?.let { code ->
                if (code.isBlank()) {
                    binding.textViewVerifyCodeToSignUpFeedback.text =
                        getString(R.string.error_verify_code_to_sign_up_blank_verification_code)
                } else {
                    viewModel.verifyCode(email, password, code)
                }
            }
        }

    }
}