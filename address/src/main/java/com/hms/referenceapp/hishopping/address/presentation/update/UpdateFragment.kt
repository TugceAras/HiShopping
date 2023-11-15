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
package com.hms.referenceapp.hishopping.address.presentation.update

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hms.referenceapp.hishopping.address.R
import com.hms.referenceapp.hishopping.address.databinding.FragmentUpdateBinding
import com.hms.referenceapp.hishopping.address.presentation.add.AddFragmentArgs
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.data.model.UserAddress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateFragment : BaseFragment<UpdateViewModel, FragmentUpdateBinding>() {
    override var layoutRes: Int = R.layout.fragment_update

    private val args: AddFragmentArgs by navArgs()

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        args.address?.let {
            viewModel.setAddress(it)
        }

        binding.layoutAddress.editTextAddressTitleInput.requestFocus()
        binding.buttonUpdate.setOnClickListener {
            viewModel.updateAddress(
                UserAddress(
                    binding.layoutAddress.editTextAddressTitleInput.text.toString(),
                    binding.layoutAddress.editTextAddressLineInput.text.toString(),
                    binding.layoutAddress.editTextAddressCityInput.text.toString(),
                    binding.layoutAddress.editTextCountryInput.text.toString(),
                    binding.layoutAddress.editTextAddressBuildingNoInput.text.toString(),
                    binding.layoutAddress.editTextAddressAptNoInput.text.toString()
                )
            )
            findNavController().navigate(R.id.action_updateFragment_pop_including_addressFragment)
        }
    }

    override fun observeViewModel() {
        viewModel.address.observe(this) {
            binding.layoutAddress.editTextAddressTitleInput.setText(it.title)
            binding.layoutAddress.editTextAddressLineInput.setText(it.addressLine)
            binding.layoutAddress.editTextAddressCityInput.setText(it.province)
            binding.layoutAddress.editTextCountryInput.setText(it.country)
            binding.layoutAddress.editTextAddressBuildingNoInput.setText(it.buildingNo)
            binding.layoutAddress.editTextAddressAptNoInput.setText(it.aptNo)
        }
    }
}