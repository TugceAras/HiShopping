package com.hms.referenceapp.hishopping.app.categories.presentation.model

import com.hms.referenceapp.hishopping.data.model.CategoryEntity
import com.hms.referenceapp.hishopping.data.model.ProductEntity

/**
 * Created by Mustafa Kemal Özdemir on 3/28/2022.
 */
sealed class CategoryCommand {
   data class LoadCategories(val data: Map<CategoryEntity, List<ProductEntity>>): CategoryCommand()
   data class ToggleLoadingDialog(val visible: Boolean): CategoryCommand()
}