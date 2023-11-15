package com.hms.referenceapp.hishopping.app.explore.lastvisited.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewLastVisitedBinding


class LastVisitedRecyclerViewAdapter(
    private var productList: MutableList<Product>,
) :
    RecyclerView.Adapter<LastVisitedRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (productId: Int, imageView: View, transitionName: String) -> Unit =
        { _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewLastVisitedBinding
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

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewLastVisitedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: Product,
            position: Int,
            onItemSelected: (productId: Int, imageView: View, transitionName: String) -> Unit
        ) {

            binding.imageViewLastVisitedProduct.load(item.image[0])
            binding.imageViewLastVisitedProduct.tag="L_V_${item.id}"
            binding.textViewLastVisitedPrice.text = item.price.toString() + "$"
            binding.textViewLastVisitedTitle.text = item.title
            binding.imageViewLastVisitedProduct.transitionName =  binding.imageViewLastVisitedProduct.tag.toString()
            binding.cardViewLastVisited.setOnClickListener {
                onItemSelected.invoke(item.id , binding.imageViewLastVisitedProduct , binding.imageViewLastVisitedProduct.tag.toString())
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
