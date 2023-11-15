package com.hms.referenceapp.hishopping.app.explore.trywithar.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewTryWithArBinding


class TryWithArRecyclerViewAdapter(
    private var productList: MutableList<Product>,
) :
    RecyclerView.Adapter<TryWithArRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (productId: Int, imageView: View, transitionName: String) -> Unit =
        { _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewTryWithArBinding
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

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewTryWithArBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: Product,
            position: Int,
            onItemSelected: (productId: Int, imageView: View, transitionName: String) -> Unit
        ) {
            binding.imageViewTryWithArProduct.load(item.image[0])
            binding.imageViewTryWithArProduct.tag = "A_R_${item.id}"
            binding.textViewTryWithArPrice.text = item.price.toString() + "$"
            binding.textViewTryWithArTitle.text = item.title
            binding.imageViewTryWithArProduct.transitionName =
                binding.imageViewTryWithArProduct.tag.toString()
            binding.cardViewTryWithAr.setOnClickListener {
                onItemSelected.invoke(
                    item.id,
                    binding.imageViewTryWithArProduct,
                    binding.imageViewTryWithArProduct.tag.toString()
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
