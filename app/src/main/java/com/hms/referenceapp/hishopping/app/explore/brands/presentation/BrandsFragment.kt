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
package com.hms.referenceapp.hishopping.app.explore.brands.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.databinding.FragmentBrandsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrandsFragment : BaseFragment<BrandsViewModel, FragmentBrandsBinding>() {


    override var layoutRes: Int = R.layout.fragment_brands
    private lateinit var adapterBrandsRecyclerView: BrandsRecyclerViewAdapter


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        arrangeBrandsRecyclerView()
        observeResultLiveData(viewModel.fetchBrands()) {
            it.let {
                it?.let { it1 -> adapterBrandsRecyclerView.updateDataSource(it1.toMutableList()) }
            }
        }
    }

    private fun arrangeBrandsRecyclerView() {
        adapterBrandsRecyclerView = BrandsRecyclerViewAdapter(mutableListOf())
        with(binding.brandsRecyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterBrandsRecyclerView
        }
        adapterBrandsRecyclerView.onItemSelected = { position, item ->

        }
    }

    override fun observeViewModel() {}

}