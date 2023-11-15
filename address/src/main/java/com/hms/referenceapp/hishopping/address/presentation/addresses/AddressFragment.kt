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
package com.hms.referenceapp.hishopping.address.presentation.addresses

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.lib.commonmobileservices.identity.IdentityService
import com.hms.lib.commonmobileservices.identity.model.UserAddressResponse
import com.hms.referenceapp.hishopping.address.R
import com.hms.referenceapp.hishopping.address.databinding.FragmentAddressBinding
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.makeVisible
import com.hms.referenceapp.hishopping.core.getString
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.data.model.UserAddress
import com.hms.referenceapp.hishopping.signin.SignInManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : BaseFragment<AddressViewModel, FragmentAddressBinding>() {
    override var layoutRes: Int = R.layout.fragment_address

    private val args: AddressFragmentArgs by navArgs()
    private lateinit var signInManager: SignInManager
    private lateinit var adapterAddressRecyclerView: AddressRecyclerViewAdapter
    private var identityService: IdentityService? = null
    private lateinit var loadingDialog: LoadingDialog

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        signInManager = SignInManager(requireActivity())
        identityService = IdentityService.Factory.create(requireActivity())
        loadingDialog = LoadingDialog.Builder(requireContext()).build()
        signInManager.getUser()?.let { viewModel.fetchAddresses(it.id) }
    }

    override fun arrangeUI() {
        if (args.user.providerType == "huawei") {
            binding.containerIdentity.makeVisible()
            binding.viewIdentitySeparator.makeVisible()
        }

        arrangeAddressRecyclerView()

        binding.containerManuel.setOnClickListener { navigateToAddFragment(null) }
        binding.containerMap.setOnClickListener {
            findNavController().navigate(
                AddressFragmentDirections.actionAddressFragmentToMapFragment()
            )
        }
        binding.containerIdentity.setOnClickListener {
            identityService!!.getUserAddress()
        }
    }

    private fun arrangeAddressRecyclerView() {
        adapterAddressRecyclerView = AddressRecyclerViewAdapter(mutableListOf())
        with(binding.recyclerViewAddress) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterAddressRecyclerView
            addItemDecoration(
                MyItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL,
                    32
                )
            )
        }
        adapterAddressRecyclerView.onItemSelected = { position, userAddress ->
            navigateToAddFragment(userAddress)
        }
        adapterAddressRecyclerView.onItemDeleted = { position, userAddress ->
            viewModel.deleteAddress(userAddress)
        }
    }

    override fun observeViewModel() {
        viewModel.identityAddress.observe(viewLifecycleOwner) { address ->
            address?.let { navigateToAddFragment(it) }
        }
        viewModel.uiState.observe(viewLifecycleOwner){ uiState ->
            uiState.addressList?.let { adapterAddressRecyclerView.updateDataSource(it.toMutableList()) }
            if (uiState.loading) loadingDialog.show()
            else loadingDialog.hide()
            uiState.error?.let {
                Toast.makeText(requireContext(),it.getString(requireContext()),Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IdentityService.requestCode -> when (resultCode) {
                Activity.RESULT_OK -> {
                    val userAddress = UserAddressResponse().parseIntent(data)
                    viewModel.setAddress(userAddress)
                    println("$userAddress")
                }
                Activity.RESULT_CANCELED -> {
                }
                else -> {
                }
            }
            else -> {
            }
        }
    }

    private fun navigateToAddFragment(address: UserAddress? = null) {
        if (address == null) {
            findNavController().navigate(
                AddressFragmentDirections.actionAddressFragmentToAddFragment()
            )
        } else {
            findNavController().navigate(
                AddressFragmentDirections.actionAddressFragmentToAddFragment(
                    address
                )
            )
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.setAddress(null)
    }
}