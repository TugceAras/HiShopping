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
package com.hms.referenceapp.hishopping.app.explore.lastvisited.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.explore.ExploreFragmentDirections
import com.hms.referenceapp.hishopping.app.explore.visited.presentation.LastVisitedViewModel
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.databinding.FragmentLastVisitedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LastVisitedFragment : BaseFragment<LastVisitedViewModel, FragmentLastVisitedBinding>() {

    override var layoutRes: Int = R.layout.fragment_last_visited
    private lateinit var adapterLastVisitedRecyclerView: LastVisitedRecyclerViewAdapter

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        arrangeLastVisitedRecyclerView()
        observeResultLiveData(viewModel.fetchLastVisitedItems()) {
            it?.let { it1 -> adapterLastVisitedRecyclerView.updateDataSource(it1.toMutableList()) }
        }

    }

    private fun arrangeLastVisitedRecyclerView() {
        adapterLastVisitedRecyclerView = LastVisitedRecyclerViewAdapter(mutableListOf())
        with(binding.recyclerViewLastVisited) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterLastVisitedRecyclerView
        }
        adapterLastVisitedRecyclerView.onItemSelected = { productId, imageView, transitionName ->
            val extras = FragmentNavigatorExtras(
                imageView to transitionName
            )
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreToDetailNavGraph(productId, transitionName),
                extras
            )
        }
    }

    override fun observeViewModel() {}


}