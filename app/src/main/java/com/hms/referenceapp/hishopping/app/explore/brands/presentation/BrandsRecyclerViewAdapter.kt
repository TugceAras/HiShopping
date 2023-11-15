package com.hms.referenceapp.hishopping.app.explore.brands.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.data.model.Brand
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewBrandBinding


class BrandsRecyclerViewAdapter(
    private var brandList: MutableList<Brand>,
) :
    RecyclerView.Adapter<BrandsRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: Brand) -> Unit =
        { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewBrandBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return brandList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        brandList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: Brand,
            position: Int,
            onItemSelected: (position: Int, item: Brand) -> Unit
        ) {
            binding.imageViewBrand.load(
                getImageResource(
                    holder.itemView.rootView.context,
                    item.image
                )
            )

            binding.imageViewBrand.setOnClickListener {
                onItemSelected.invoke(position, item)
            }
        }

        private fun getImageResource(context: Context, image: String?): Int {
            return context.resources.getIdentifier(image!!, "mipmap", context.packageName)
        }

    }

    fun updateDataSource(
        newDataSource: MutableList<Brand>,
    ) {
        this.brandList = newDataSource
        notifyDataSetChanged()
    }


}
