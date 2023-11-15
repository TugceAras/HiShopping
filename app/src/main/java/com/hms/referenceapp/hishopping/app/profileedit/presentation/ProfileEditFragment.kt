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

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.hms.lib.commonmobileservices.auth.AuthService
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.core.UIText
import com.hms.referenceapp.hishopping.core.getString
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.databinding.FragmentProfileEditBinding
import com.hms.referenceapp.hishopping.signin.SignInManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment : BaseFragment<ProfileEditViewModel, FragmentProfileEditBinding>() {

    override var layoutRes: Int = R.layout.fragment_profile_edit
    private lateinit var signInManager: SignInManager
    private lateinit var loadingDialog: LoadingDialog
    private val REQUEST_CODE = 100
    private var authService: AuthService ?= null

    companion object {
        private const val RC_SIGN_IN = 1000
        private const val TAG = "ProfileEditFragment"
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        authService = AuthService.Factory.create(requireActivity())
        signInManager = SignInManager(requireActivity())
        loadingDialog = LoadingDialog.Builder(requireContext()).build()

        Log.i(TAG, "viewCreated: ${signInManager.getUser()}")

        onGetCodeButtonClick()
        onChangeEmailButtonClick()
        onChangePasswordButtonClick()
        onChangeUsernameButtonClick()
        onChangePhotoButtonClick()
    }

    override fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner){ uiState ->
            uiState.successMessage?.let { showSuccessMessage(it) }
            uiState.errorMessage?.let { showErrorMessage(it) }
            if (uiState.noMessage) deleteMessageText()
            if (uiState.loading) loadingDialog.show()
            else loadingDialog.hide()
        }
    }

    private fun onGetCodeButtonClick() {
        signInManager.getUser()?.let {
            if (it.providerType == "email")
                binding.buttonProfileEditChangeEmail.isEnabled = false
        }
        binding.buttonProfileEditGetCodePassword.setOnClickListener {
            viewModel.getCodeForPassword(
                binding.editTextProfileEditEmail.text.toString()
            )
            emptyTextInput(binding.editTextProfileEditEmail)
        }
    }

    private fun onChangeEmailButtonClick() {
        binding.buttonProfileEditChangeEmail.setOnClickListener {
            viewModel.changeEmail(
                signInManager.getUser()?.id,
                binding.editTextProfileEditEmail.text.toString(),
            )
            emptyTextInput(binding.editTextProfileEditEmail)
        }
    }

    private fun onChangePasswordButtonClick() {
        binding.buttonProfileEditChangePassword.setOnClickListener {
            viewModel.changePassword(
                binding.editTextProfileEditPassword.text.toString(),
                binding.editTextProfileEditCode.text.toString()
            )
            emptyTextInput(binding.editTextProfileEditPassword)
            emptyTextInput(binding.editTextProfileEditCode)
        }
    }

    private fun onChangeUsernameButtonClick() {
        binding.buttonProfileEditChangeUsername.setOnClickListener {
            signInManager.getUser()?.let {
                viewModel.updateFullName(
                    it.id,
                    binding.editTextProfileEditUsername.text.toString()
                )
            }?:run{showErrorMessage(UIText.ResourceText(R.string.unexpected_error))}
            emptyTextInput(binding.editTextProfileEditUsername)
        }
    }

    private fun onChangePhotoButtonClick() {
        binding.buttonProfileEditChangePhoto.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            authService!!.updatePhoto(data?.data.toString())
        }
    }

    private fun showSuccessMessage(uiText:UIText){
        binding.textViewProfileEditMessage.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorAcceptGreen))
        binding.textViewProfileEditMessage.text = uiText.getString(requireContext())
    }

    private fun showErrorMessage(uiText:UIText){
        binding.textViewProfileEditMessage.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorDeclineRed))
        binding.textViewProfileEditMessage.text = uiText.getString(requireContext())
    }

    private fun deleteMessageText(){
        binding.textViewProfileEditMessage.text = ""
    }

    private fun emptyTextInput(editText:TextInputEditText){
        editText.setText("")
    }
}