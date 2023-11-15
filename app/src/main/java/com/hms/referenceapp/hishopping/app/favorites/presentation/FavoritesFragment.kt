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
package com.hms.referenceapp.hishopping.app.favorites.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.showToast
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.databinding.FragmentFavoritesBinding
import com.hms.referenceapp.hishopping.signin.SignInManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FavoritesViewModel,FragmentFavoritesBinding>() {
    override val layoutRes: Int = R.layout.fragment_favorites
    private val favoritesAdapter = FavoritesRecyclerViewAdapter()
    private lateinit var signInManager: SignInManager
    private var loadingDialog: LoadingDialog? = null

    override fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner){ uiState ->
            uiState.favoriteProductList?.let { favoritesAdapter.differ.submitList(it) }
            when {
                uiState.loading -> loadingDialog?.show()
                uiState.error -> {
                    uiState.errorMessage?.let {
                        showToast(it)
                    }
                }
                else -> loadingDialog?.hide()
            }
        }
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        signInManager = SignInManager(requireActivity())
        loadingDialog = LoadingDialog.Builder(requireContext()).build()
        setupRecyclerView()
        signInManager.getUser()?.let { viewModel.getFavoriteProducts(it.id) }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFavorite.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoritesAdapter
        }
        favoritesAdapter.setOnItemClickListener {
            viewModel.deleteFromFavorites(it)
        }
    }
}