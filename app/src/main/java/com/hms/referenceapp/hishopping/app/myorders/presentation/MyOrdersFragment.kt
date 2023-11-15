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
package com.hms.referenceapp.hishopping.app.myorders.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.data.model.Order
import com.hms.referenceapp.hishopping.databinding.FragmentMyOrdersBinding
import com.hms.referenceapp.hishopping.signin.SignInManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrdersFragment : BaseFragment<MyOrdersViewModel, FragmentMyOrdersBinding>() {

    override var layoutRes: Int = R.layout.fragment_my_orders
    private lateinit var ordersRecyclerViewAdapter: OrdersRecyclerViewAdapter
    private lateinit var signInManager: SignInManager
    private var loadingDialog: LoadingDialog? = null

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        signInManager = SignInManager(requireActivity())
        loadingDialog = LoadingDialog.Builder(requireContext()).build()
        arrangeMyOrdersRecyclerView()
        signInManager.getUser()?.let { viewModel.getAllOrders(it.id) }
    }

    override fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner){ uiState ->
            ordersRecyclerViewAdapter.differ.submitList(uiState.orderList)
            if (uiState.loading) loadingDialog?.show() else loadingDialog?.hide()
        }
    }

    private fun arrangeMyOrdersRecyclerView() {
        ordersRecyclerViewAdapter = OrdersRecyclerViewAdapter()
        binding.recyclerViewMyOrders.apply {
            adapter = ordersRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        ordersRecyclerViewAdapter.setOnItemClickListener { orderEntity ->
            if (orderEntity.orderStatus == Order.OrderStatus.TRANSPORT)
            findNavController().navigate(
                MyOrdersFragmentDirections.actionMyOrdersToCourierTrack(
                    orderId = orderEntity.id,
                    orderEntity = orderEntity
                )
            )else{
                Toast.makeText(
                    requireContext(),
                    orderEntity.orderStatus.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}