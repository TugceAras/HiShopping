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
package com.hms.referenceapp.hishopping.address.presentation.add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hms.referenceapp.hishopping.address.R
import com.hms.referenceapp.hishopping.address.databinding.FragmentAddBinding
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.core.getString
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.data.model.UserAddress
import com.hms.referenceapp.hishopping.signin.SignInManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddFragment : BaseFragment<AddViewModel, FragmentAddBinding>() {

    override var layoutRes: Int = R.layout.fragment_add
    private val args: AddFragmentArgs by navArgs()
    private lateinit var signInManager: SignInManager
    private lateinit var loadingDialog: LoadingDialog

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        signInManager = SignInManager(requireActivity())
        loadingDialog = LoadingDialog.Builder(requireContext()).build()
        setUserIfExist()

        binding.layoutAddress.editTextAddressTitleInput.requestFocus()
        binding.buttonAddSave.setOnClickListener {
            viewModel.addAddress(
                UserAddress(
                    id = args.address?.id ?: UUID.randomUUID().toString(),
                    userId = signInManager.getUser()?.id,
                    title = binding.layoutAddress.editTextAddressTitleInput.text.toString(),
                    addressLine = binding.layoutAddress.editTextAddressLineInput.text.toString(),
                    province = binding.layoutAddress.editTextAddressCityInput.text.toString(),
                    country = binding.layoutAddress.editTextCountryInput.text.toString(),
                    buildingNo = binding.layoutAddress.editTextAddressBuildingNoInput.text.toString(),
                    aptNo = binding.layoutAddress.editTextAddressAptNoInput.text.toString()
                ),
                requireContext()
            )
        }
    }

    override fun observeViewModel() {
        viewModel.address.observe(this) { address ->
            address.title?.let { binding.layoutAddress.editTextAddressTitleInput.setText(it) }
            address.addressLine?.let { binding.layoutAddress.editTextAddressLineInput.setText(it) }
            address.country?.let { binding.layoutAddress.editTextCountryInput.setText(it) }
            address.province?.let { binding.layoutAddress.editTextAddressCityInput.setText(it) }
            address.buildingNo?.let { binding.layoutAddress.editTextAddressBuildingNoInput.setText(it) }
            address.aptNo?.let { binding.layoutAddress.editTextAddressAptNoInput.setText(it) }
        }
        viewModel.uiState.observe(viewLifecycleOwner){ uiState ->
            if (uiState.success) findNavController().navigate(R.id.action_addFragment_pop_including_addressFragment)
            if (uiState.loading) loadingDialog.show() else loadingDialog.hide()
            uiState.error?.let { Toast.makeText(requireContext(),it.getString(requireContext()),Toast.LENGTH_SHORT).show() }
        }
    }

    private fun setUserIfExist() {
        args.address?.let { viewModel.setAddress(it) }
    }
}