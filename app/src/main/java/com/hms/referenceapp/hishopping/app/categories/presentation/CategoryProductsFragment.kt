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
package com.hms.referenceapp.hishopping.app.categories.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.categories.presentation.adapter.CategoryMainAdapter
import com.hms.referenceapp.hishopping.app.categories.presentation.model.CategoryCommand
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.data.model.CategoryEntity
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.data.model.ProductEntity
import com.hms.referenceapp.hishopping.databinding.FragmentCategoryProductBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CategoryProductsFragment :
    BaseFragment<CategoriesViewModel, FragmentCategoryProductBinding>() {

    override var layoutRes: Int = R.layout.fragment_category_product
    private var loadingDialog: LoadingDialog? = null

    private val selectedCategoriesName: List<String> = arrayListOf(
        "sport", "winter",
        "jewelery", "electronics", "former", "women", "men", "casual", "office", "car"
    )

    private lateinit var adapterCategoryProductsRecyclerView: CategoryProductsRecyclerViewAdapter
    private lateinit var categoryMainAdapter: CategoryMainAdapter

    override fun observeViewModel() {
        viewModel.commands.singleObserve(viewLifecycleOwner) {
            when(it) {
                is CategoryCommand.LoadCategories -> setupGridRecyclerViewData(it.data)
                is CategoryCommand.ToggleLoadingDialog -> toggleLoadingDialog(it.visible)
            }
        }
        /*observeResultLiveData(viewModel.getCategoryProducts(selectedCategoriesName)) {
            it?.let {
                setSectionAdapter(it)
            }
        }*/
    }

    private fun toggleLoadingDialog(visible: Boolean) {
        loadingDialog?.let {
            if(!visible) {
                it.hide()
                loadingDialog = null
            }
        }?: run {
            if(visible) {
                loadingDialog = LoadingDialog.Builder(requireContext()).build()
                loadingDialog?.show()
            }
        }

    }


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        //arrangeCategoryRecyclerView()
        setupGridRecyclerView()
        viewModel.getCategoryProducts()
    }

    private fun setupGridRecyclerView() {
        //val productsAdapter = CategoryProductsAdapter(requireContext())
        categoryMainAdapter = CategoryMainAdapter(requireContext())

        binding.recyclerViewCategoryProduct.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoryMainAdapter
        }
    }

    private fun setupGridRecyclerViewData(data: Map<CategoryEntity, List<ProductEntity>>) {
        categoryMainAdapter.loadData(data)
    }

    private fun arrangeCategoryRecyclerView() {
        adapterCategoryProductsRecyclerView = CategoryProductsRecyclerViewAdapter(mutableListOf())
        with(binding.recyclerViewCategoryProduct) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = adapterCategoryProductsRecyclerView
        }
        adapterCategoryProductsRecyclerView.onItemSelected =
            { position, item, imageView, transitionName ->
                val extras = FragmentNavigatorExtras(
                    imageView to transitionName
                )
                findNavController().navigate(
                    CategoryProductsFragmentDirections.actionCategoriesToDetailNavGraph(
                        item.id,
                        transitionName
                    ), extras
                )
            }
    }

    private fun setSectionAdapter(list: List<Product>) {

        val sections: MutableList<CategorySectionedGridAdapter.Section> = arrayListOf()
        val items: MutableList<Product> = arrayListOf()
        val groupList = list.groupBy {
            it.category
        }
        val mSectionedAdapter =
            CategorySectionedGridAdapter(
                this.requireContext(),
                R.layout.item_recyclerview_category_products_section,
                R.id.textViewCategoryProductsSection,
                binding.recyclerViewCategoryProduct,
                adapterCategoryProductsRecyclerView
            )
        var offset = 0
        groupList.onEachIndexed { index, entry ->
            sections.add(
                CategorySectionedGridAdapter.Section(
                    offset,
                    entry.key.toUpperCase(Locale.ROOT)
                )
            )
            items.addAll(entry.value)
            offset += entry.value.size
        }
        mSectionedAdapter.setSections(sections)
        adapterCategoryProductsRecyclerView.updateDataSource(items)
        //Apply this adapter to the RecyclerView
        binding.recyclerViewCategoryProduct.adapter = mSectionedAdapter
    }
}