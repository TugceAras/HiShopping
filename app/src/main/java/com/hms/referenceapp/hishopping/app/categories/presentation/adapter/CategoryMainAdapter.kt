package com.hms.referenceapp.hishopping.app.categories.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.data.model.CategoryEntity
import com.hms.referenceapp.hishopping.data.model.ProductEntity
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewCategoryListBinding
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewCategoryProductsSectionBinding

/**
 * Created by Mustafa Kemal Özdemir on 3/28/2022.
 */
class CategoryMainAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList = mutableListOf<RecyclerViewElement>()
    internal var onItemSelected: (position: Int, item: ProductEntity, imageView: View, transitionName: String) -> Unit =
        { _, _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return  when(ItemType.getEnumByValue(viewType)) {
            ItemType.CATEGORY_TITLE -> {
                val view = ItemRecyclerviewCategoryProductsSectionBinding.inflate(inflater, parent, false)
                CategoryTitleViewHolder(view)
            }
            ItemType.CATEGORY_ITEM -> {
                val view = ItemRecyclerviewCategoryListBinding.inflate(inflater, parent, false)
                CategoryProductsViewHolder(view)
            }
            ItemType.BLANK_BOX -> {
                val view = ItemRecyclerviewCategoryProductsSectionBinding.inflate(inflater, parent, false)
                CategoryTitleViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(dataList[position].itemType) {
            ItemType.CATEGORY_TITLE -> {
                (holder as CategoryTitleViewHolder).bindHeader(dataList[position].content as CategoryEntity)
            }
            ItemType.CATEGORY_ITEM -> {
                (holder as CategoryProductsViewHolder).bindProduct(dataList[position].content as ProductEntity, onItemSelected)
            }
            ItemType.BLANK_BOX -> Unit
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].itemType.value
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadData(data: Map<CategoryEntity, List<ProductEntity>>) {
        dataList.clear()
        data.forEach { (category, items) ->
            dataList.add(RecyclerViewElement(ItemType.CATEGORY_TITLE, category))
            dataList.add(RecyclerViewElement(ItemType.BLANK_BOX, ItemType.BLANK_BOX.value))
            if(dataList.size % 2 == 1) {
                dataList.add(dataList.size - 2, RecyclerViewElement(ItemType.BLANK_BOX, ItemType.BLANK_BOX.value))
            }
            items.forEach {
                dataList.add(RecyclerViewElement(ItemType.CATEGORY_ITEM, it))
            }
        }
        notifyDataSetChanged()
    }

    inner class CategoryTitleViewHolder(val binding: ItemRecyclerviewCategoryProductsSectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindHeader(categoryEntity: CategoryEntity) {
            binding.textViewCategoryProductsSection.text = categoryEntity.name
        }
    }

    inner class CategoryProductsViewHolder(val binding: ItemRecyclerviewCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindProduct(
            product: ProductEntity,
            onItemSelected: (position: Int, item: ProductEntity, imageView: View, transitionName: String) -> Unit,
        ) {
            binding.apply {
                product.images.takeIf{it.isNotEmpty()}?.let {
                    imageViewCategoryList.load(it.first().url)
                }
                imageViewCategoryList.tag = binding.root.context.getString( R.string.category_tag,
                    product.id
                )
                imageViewCategoryList.transitionName = imageViewCategoryList.tag.toString()

                textViewCategoryListDescription.text = product.title
                textViewCategoryListPrice.text = binding.root.context.resources.getString(
                    R.string.category_price,
                    product.price.toString(),
                    "$"
                )
                binding.root.setOnClickListener {
                    onItemSelected.invoke(
                        adapterPosition,
                        product,
                        imageViewCategoryList,
                        imageViewCategoryList.tag.toString()
                    )
                }
            }
        }
    }

    private enum class ItemType(val value: Int) {
        CATEGORY_TITLE(0), CATEGORY_ITEM(1), BLANK_BOX(2);

        companion object {
            val map = ItemType.values().associateBy(ItemType::value)
            fun getEnumByValue(value: Int): ItemType {
                //Assert
                return map[value]!!
            }
        }
    }

    private data class RecyclerViewElement(
        val itemType: ItemType,
        val content: Any
    )
}