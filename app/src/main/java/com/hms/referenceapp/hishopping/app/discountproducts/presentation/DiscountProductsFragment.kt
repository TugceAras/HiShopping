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
package com.hms.referenceapp.hishopping.app.discountproducts.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.explore.ExploreFragmentDirections
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.databinding.FragmentDiscountProductsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscountProductsFragment :
    BaseFragment<DiscountProductsViewModel, FragmentDiscountProductsBinding>() {

    override var layoutRes: Int = R.layout.fragment_discount_products

    private lateinit var adapterDisCountProductsRecyclerView: DiscountProductsRecyclerViewAdapter

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        arrangeDiscountProductRecyclerView()
        observeResultLiveData(viewModel.getDiscountProducts(15)) {
            it?.let {
                adapterDisCountProductsRecyclerView.updateDataSource(it.toMutableList())
            }
        }
    }

    private fun arrangeDiscountProductRecyclerView() {
        adapterDisCountProductsRecyclerView = DiscountProductsRecyclerViewAdapter(mutableListOf())
        with(binding.recyclerViewDiscountProducts) {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = adapterDisCountProductsRecyclerView
        }
        adapterDisCountProductsRecyclerView.onItemSelected =
            { position, item, imageView, transitionName ->
                val extras = FragmentNavigatorExtras(
                    imageView to transitionName
                )
                findNavController().navigate(
                    ExploreFragmentDirections.actionExploreToDetailNavGraph(
                        item.id,
                        transitionName
                    ),
                    extras
                )
            }
    }

    override fun observeViewModel() {}
}