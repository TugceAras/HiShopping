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
package com.hms.referenceapp.hishopping.app.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.discountproducts.presentation.DiscountProductsFragment
import com.hms.referenceapp.hishopping.app.explore.brands.presentation.BrandsFragment
import com.hms.referenceapp.hishopping.app.explore.campaigns.presentation.CampaignsFragment
import com.hms.referenceapp.hishopping.app.explore.trywithar.presentation.TryWithArFragment
import com.hms.referenceapp.hishopping.app.explore.lastvisited.presentation.LastVisitedFragment
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.databinding.FragmentExploreBinding
import com.hms.referenceapp.hishopping.productadvisor.view.ProductAdvisorFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : BaseFragment<ExploreViewModel, FragmentExploreBinding>(),
    ProductAdvisorFragment.ProductAdviserFragmentHolder {

    override var layoutRes: Int = R.layout.fragment_explore
    private var lastScrollPosition = intArrayOf(0, 0)

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {

        loadProductAdvisorFragment(1)
        loadCampaignsFragment()
        loadBrandsFragment()
        loadLastVisitedProductsFragment()
        loadSearchFragment()
        loadTryWithArFragment()
        loadDiscountProductsFragment()

        binding.nestedScrollViewExplore.apply {
            scrollTo(lastScrollPosition[0], lastScrollPosition[1])
            setOnScrollChangeListener { _, scrollX, scrollY, _, _ ->
                lastScrollPosition[0] = scrollX
                lastScrollPosition[1] = scrollY
            }
        }
    }

    private fun loadProductAdvisorFragment(orientationType: Int) =
        loadChildFragment(binding.frameLayoutExploreAdvisor.id) {
            ProductAdvisorFragment().apply {
                arguments = Bundle().apply {
                    putInt("orientationType", orientationType)
                }
            }
        }

    private fun loadCampaignsFragment() =
        loadChildFragment(binding.frameLayoutExploreCampaigns.id) {
            CampaignsFragment()
        }


    private fun loadBrandsFragment() =
        loadChildFragment(binding.frameLayoutExploreBrands.id) {
            BrandsFragment()
        }

    private fun loadLastVisitedProductsFragment() =
        loadChildFragment(binding.frameLayoutExploreLastVisitedProducts.id) {
            LastVisitedFragment()
        }

    private fun loadSearchFragment() {
        binding.textViewExploreSearch.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.cardViewExploreSearch to "transition_name"
            )
            findNavController().navigate(R.id.search, null, null, extras)
        }
    }

    private fun loadTryWithArFragment() =
        loadChildFragment(binding.frameLayoutExploreTryWithAr.id) {
            TryWithArFragment()
        }

    private fun loadDiscountProductsFragment() =
        loadChildFragment(binding.frameLayoutExploreDiscountProducts.id) {
            DiscountProductsFragment()
        }

    private inline fun <reified T : Fragment> loadChildFragment(
        layout: Int,
        fragmentCreator: () -> T
    ) {
        childFragmentManager.fragments.firstOrNull {
            it is T
        } ?: run {
            childFragmentManager.beginTransaction().replace(layout, fragmentCreator.invoke())
                .commit()
        }
    }

    override fun advisedProductItemClicked(
        productId: Int,
        imageView: View,
        transitionName: String
    ) {
        val extras = FragmentNavigatorExtras(
            imageView to transitionName
        )
        findNavController().navigate(
            ExploreFragmentDirections.actionExploreToDetailNavGraph(productId, transitionName),
            extras
        )

    }

    override fun observeViewModel() {}
}