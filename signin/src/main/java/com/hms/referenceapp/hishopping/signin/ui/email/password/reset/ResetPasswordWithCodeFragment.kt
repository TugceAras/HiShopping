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
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hms.lib.commonmobileservices.core.*
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.hideKeyboard
import com.hms.referenceapp.hishopping.signin.AUTH_SERVICE_ERROR_INVALID_VERIFICATION_CODE
import com.hms.referenceapp.hishopping.signin.databinding.FragmentResetPasswordWithCodeBinding
import com.hms.referenceapp.hishopping.signin.util.isPasswordValid
import com.hms.referenceapp.hishopping.signin.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordWithCodeFragment :
    BaseFragment<ResetPasswordWithCodeViewModel, FragmentResetPasswordWithCodeBinding>() {
    override val layoutRes: Int = R.layout.fragment_reset_password_with_code
    private val args: ResetPasswordWithCodeFragmentArgs by navArgs()

    private lateinit var email: String
    private lateinit var verificationCode: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String

    override fun observeViewModel() {
        viewModel.resetPasswordResult.observe(viewLifecycleOwner) {
            when (it) {
                is ResultData.Loading -> {}
                is ResultData.Success -> {
                    val action =
                        ResetPasswordWithCodeFragmentDirections.actionResetPasswordWithCodeFragmentToSignInWithEmailFragment(
                            email,
                            password
                        )
                    findNavController().navigate(action)
                }
                is ResultData.Failed -> {
                    if (it.error!!.contains(AUTH_SERVICE_ERROR_INVALID_VERIFICATION_CODE)) {
                        binding.textViewResetPasswordWithCodeFeedback.text =
                            getString(R.string.error_wrong_verification_code_message)
                    }
                }
            }
        }
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        email = args.email

        binding.editTextVerificationCode.transformationMethod = null
        binding.buttonResetPasswordWithCode.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        this.hideKeyboard()
        verificationCode = binding.editTextVerificationCode.text.toString()
        password = binding.editTextResetPasswordWithCodePassword.text.toString()
        passwordConfirm = binding.editTextPasswordResetPasswordWithCodeConfirm.text.toString()

        if (isPasswordValid(password)) {
            if (password == passwordConfirm) {
                viewModel.resetPassword(email, password, verificationCode)
            } else {
                binding.textViewResetPasswordWithCodeFeedback.text =
                    getString(R.string.error_passwords_mismatch_message)
            }
        } else {
            binding.textViewResetPasswordWithCodeFeedback.text =
                getString(R.string.error_email_wrong_password_format_message)
        }
    }
}