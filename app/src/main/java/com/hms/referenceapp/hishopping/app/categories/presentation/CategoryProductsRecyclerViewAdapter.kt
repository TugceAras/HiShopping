package com.hms.referenceapp.hishopping.app.categories.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewCategoryListBinding


class CategoryProductsRecyclerViewAdapter(
    private var productList: MutableList<Product>,
) : RecyclerView.Adapter<CategoryProductsRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: Product, imageView: View, transitionName: String) -> Unit =
        { _, _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewCategoryListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        productList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewCategoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: Product,
            position: Int,
            onItemSelected: (position: Int, item: Product, imageView: View, transitionName: String) -> Unit,
        ) {
            binding.imageViewCategoryList.load(item.image[0])
            binding.imageViewCategoryList.tag = holder.itemView.context.resources.getString(
                R.string.category_tag,
                item.id.toString()
            )
            binding.textViewCategoryListDescription.text = item.title
            binding.textViewCategoryListPrice.text = holder.itemView.context.resources.getString(
                R.string.category_price,
                item.price.toString(),
                "$"
            )
            binding.imageViewCategoryList.transitionName =
                binding.imageViewCategoryList.tag.toString()
            holder.itemView.setOnClickListener {
                onItemSelected.invoke(
                    position,
                    item,
                    binding.imageViewCategoryList,
                    binding.imageViewCategoryList.tag.toString()
                )
            }
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<Product>,
    ) {
        this.productList = newDataSource
        notifyDataSetChanged()
    }

}
