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
package com.hms.referenceapp.hishopping.signin.ui.email.password.reset

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hms.lib.commonmobileservices.auth.common.VerificationType
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_NO_EXISTING_USER
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_TOO_MANY_ATTEMPTS
import com.hms.referenceapp.hishopping.signin.R
import com.hms.referenceapp.hishopping.signin.databinding.FragmentResetPasswordBinding
import com.hms.referenceapp.hishopping.signin.util.isEmailValid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment :
    BaseFragment<ResetPasswordViewModel, FragmentResetPasswordBinding>() {
    override val layoutRes: Int = R.layout.fragment_reset_password

    private lateinit var email: String
    
    companion object{
        private const val TAG = "ResetPasswordFragment"
    }

    override fun observeViewModel() {
        viewModel.emailVerificationResult.observe(viewLifecycleOwner) {
            when (it) {
                is ResultData.Loading -> {
                    Log.i(TAG, "observeViewModel: loading")
                }
                is ResultData.Success -> {
                    Log.i(TAG, "observeViewModel: success ${it.data}")
                    if (it.data == VerificationType.CODE) {
                        val action =
                            ResetPasswordFragmentDirections.actionResetPasswordFragmentToResetPasswordWithCodeFragment(
                                email
                            )
                        findNavController().navigate(action)
                    }
                    if (it.data == VerificationType.LINK) {
                        showSuccessDialog(
                            "",
                            getString(R.string.ok),
                            getString(R.string.reset_password_with_link_success_dialog_message)
                        ) {
                            findNavController().navigateUp()
                        }
                    }
                }
                is ResultData.Failed -> {
                    Log.e(TAG, "observeViewModel: error -> ${it.error}")
                    if (it.error!!.contains(AUTH_SERVICE_ERROR_NO_EXISTING_USER)) {
                        binding.textViewResetPasswordFeedback.text =
                            getString(R.string.error_sign_in_with_email_user_not_registered_message)
                    }
                    if (it.error!!.contains(AUTH_SERVICE_ERROR_TOO_MANY_ATTEMPTS)) {
                        binding.textViewResetPasswordFeedback.text =
                            getString(R.string.error_verify_code_interval_limit_message)
                    }
                }
            }
        }
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonResetPasswordContinue.setOnClickListener {
            binding.editTextResetPasswordEmail.text?.toString()?.let {
                email = it
                if(isEmailValid(email)){
                    viewModel.resetPassword(email)
                }else{
                    binding.textViewResetPasswordFeedback.text = getString(R.string.error_wrong_email_format_message)
                }
            }
        }
    }
}