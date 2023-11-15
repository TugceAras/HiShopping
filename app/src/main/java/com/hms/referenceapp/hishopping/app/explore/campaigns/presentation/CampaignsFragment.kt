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
package com.hms.referenceapp.hishopping.app.explore.campaigns.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.explore.campaigns.presentation.transformations.MarginTransformer
import com.hms.referenceapp.hishopping.app.explore.campaigns.presentation.transformations.ScaleTransformer
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.databinding.FragmentCampaignsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CampaignsFragment : BaseFragment<CampaignsViewModel, FragmentCampaignsBinding>() {

    override var layoutRes: Int = R.layout.fragment_campaigns
    private lateinit var adapterCampaignsSlider: CampaignsSliderAdapter

    private var lastPosition = -1

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        initSlider()
        observeResultLiveData(viewModel.fetchCampaigns()) {
            it.let {
                it?.let { it1 -> adapterCampaignsSlider.updateDataSource(it1.toMutableList()) }
            }
        }
    }

    private fun initSlider() {
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginTransformer(requireContext(), 10))
            addTransformer(ScaleTransformer())
        }
        adapterCampaignsSlider = CampaignsSliderAdapter(mutableListOf())
        with(binding.viewPagerCampaigns) {
            adapter = adapterCampaignsSlider
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPageTransformer(transformer)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    lastPosition = position
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    lastPosition = position
                }
            })
            if (lastPosition != -1) {
                currentItem = lastPosition
            }
        }

    }

    override fun observeViewModel() {}

}