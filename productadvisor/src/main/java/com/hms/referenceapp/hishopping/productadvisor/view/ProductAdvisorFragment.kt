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
package com.hms.referenceapp.hishopping.productadvisor.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.lib.commonmobileservices.awareness.Awareness
import com.hms.lib.commonmobileservices.awareness.service.manager.IAwarenessAPI
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.productadvisor.DataHandlerService
import com.hms.referenceapp.hishopping.productadvisor.R
import com.hms.referenceapp.hishopping.productadvisor.SHARED_PREF_ORIENTATION_TYPE
import com.hms.referenceapp.hishopping.productadvisor.databinding.FragmentProductAdvisorBinding
import com.hms.referenceapp.hishopping.productadvisor.map.AwarenessDataMapper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProductAdvisorFragment :
    BaseFragment<ProductAdvisorViewModel, FragmentProductAdvisorBinding>() {

    override val layoutRes: Int = R.layout.fragment_product_advisor

    private lateinit var adapterProductAdvisorRecyclerView: ProductAdvisorRecyclerViewAdapter

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        createProductAdvisorView()
        runWorkManager()
        val advisedProductsType = AwarenessDataMapper(this.requireActivity()).getSuggestedProducts()
        viewModel.getAllByCategory(advisedProductsType.map {
            it.name.lowercase(Locale.getDefault()).substring(0, it.name.indexOf('_'))
        })
    }

    private fun runWorkManager() {
        AwarenessDataMapper(this.requireActivity())
        IAwarenessAPI.saveClientServiceClassName(
            this.requireActivity(),
            DataHandlerService::class.java.name
        )
        Awareness(this.requireActivity()).setWorkManager()
    }

    private fun createProductAdvisorView() {
//        val orientationType = arguments?.getInt(SHARED_PREF_ORIENTATION_TYPE, 0) ?: 0
        adapterProductAdvisorRecyclerView =
            ProductAdvisorRecyclerViewAdapter(1, mutableListOf())
        with(binding.recyclerViewProductAdvisor) {
          /*  layoutManager = if (orientationType == 0) {
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            } else {
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }*/
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterProductAdvisorRecyclerView
        }
        adapterProductAdvisorRecyclerView.onItemSelected = { productId, imageView, transitionName ->
            parentFragment?.let {
                (it as? ProductAdviserFragmentHolder)?.advisedProductItemClicked(
                    productId, imageView, transitionName
                )
            }
        }

    }

    override fun observeViewModel() {
        observeResultLiveData(viewModel.productLiveData) {
            it?.let {
                adapterProductAdvisorRecyclerView.updateDataSource(it.toMutableList())
            }
        }
    }

    interface ProductAdviserFragmentHolder {
        fun advisedProductItemClicked(productId: Int, imageView: View, transitionName: String)
    }
}