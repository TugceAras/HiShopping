package com.hms.referenceapp.hishopping.app.discountproducts.presentation

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewDiscountBinding


class DiscountProductsRecyclerViewAdapter(
    private var productList: MutableList<Product>,
) : RecyclerView.Adapter<DiscountProductsRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: Product, imageView: View, transitionName: String) -> Unit =
        { _, _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewDiscountBinding
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

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewDiscountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: Product,
            position: Int,
            onItemSelected: (position: Int, item: Product, imageView: View, transitionName: String) -> Unit,
        ) {
            with(binding.imageViewDiscountItem){
                load(item.image[0])
                tag = "C_A_${item.id}"
                transitionName=  binding.imageViewDiscountItem.tag.toString()
            }

            binding.textViewDiscountItemProductDescription.text = item.title
            binding.textViewDiscountItemProductPrice.text = "Price:${item.price}$"
            with(binding.textViewDiscountItemProductRealPrice){
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text="${String.format("%.2f",item.price +(item.price*item.discountPercentage))}$"
            }
            binding.root.setOnClickListener {
                onItemSelected.invoke(
                    position,
                    item,
                    binding.imageViewDiscountItem,
                    binding.imageViewDiscountItem.tag.toString()
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
