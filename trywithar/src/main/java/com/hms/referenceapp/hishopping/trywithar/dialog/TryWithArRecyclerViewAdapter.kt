package com.hms.referenceapp.hishopping.trywithar.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.trywithar.R
import com.hms.referenceapp.hishopping.trywithar.databinding.ItemRecyclerviewDialogTryWithArProductsBinding
import com.hms.referenceapp.hishopping.trywithar.model.RenderParams


class TryWithArRecyclerViewAdapter(
    private var context: Context,
    private var renderParamsList: MutableList<RenderParams>,
) :
    RecyclerView.Adapter<TryWithArRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: RenderParams) -> Unit =
        { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewDialogTryWithArProductsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return renderParamsList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        renderParamsList[position].let {
            holder.bindItems(
                context,
                holder,
                it,
                position,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewDialogTryWithArProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            context: Context,
            holder: MainRecyclerViewHolder,
            item: RenderParams,
            position: Int,
            onItemSelected: (position: Int, Item: RenderParams) -> Unit
        ) {
            binding.imageViewTryWithArProduct.load(item.image)
            binding.textViewTryWithArTitle.text = item.title
            binding.textViewTryWithArProductPrice.text =
                context.getString(R.string.try_with_ar_product_price, item.price.toString())
            binding.constraintLayoutTryWithArProduct.setOnClickListener {
                onItemSelected.invoke(position, item)
            }
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<RenderParams>,
    ) {
        this.renderParamsList = newDataSource
        notifyDataSetChanged()
    }

 /*   fun updateList(newProducts: List<RenderParams>) {
        val diffCallBack = ProductDiffUtil(renderParams, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        diffResult.dispatchUpdatesTo(this)
        this.renderParams = newProducts
    }*/

    class ProductDiffUtil(
        private val oldList: List<RenderParams>,
        private val newList: List<RenderParams>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}

