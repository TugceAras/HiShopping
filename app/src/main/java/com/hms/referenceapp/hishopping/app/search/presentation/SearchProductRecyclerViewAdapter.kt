package com.hms.referenceapp.hishopping.app.search.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewSearchListBinding


class SearchProductRecyclerViewAdapter(
    private var productList: MutableList<Product>,
) :
    RecyclerView.Adapter<SearchProductRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, imageView: View, transitionName: String) -> Unit =
        { _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewSearchListBinding
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

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: Product,
            position: Int,
            onItemSelected: (position: Int, imageView: View, transitionName: String) -> Unit,
        ) {
            binding.imageViewSearch.load(item.image[0])
            binding.imageViewSearch.tag = "S_P_${item.id}"
            binding.textViewSearchDescription.text = item.title
            binding.textViewSearchPrice.text = "Price: ${item.price}$"
            binding.imageViewSearch.transitionName = binding.imageViewSearch.tag.toString()
            binding.cardViewSearch.setOnClickListener {
                onItemSelected.invoke(
                    item.id,
                    binding.imageViewSearch,
                    binding.imageViewSearch.tag.toString()
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
