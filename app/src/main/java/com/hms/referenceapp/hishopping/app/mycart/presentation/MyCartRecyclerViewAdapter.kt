package com.hms.referenceapp.hishopping.app.mycart.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hms.referenceapp.hishopping.data.model.CartItem
import com.hms.referenceapp.hishopping.databinding.CartListRowBinding


class MyCartRecyclerViewAdapter(
    private var cartList: MutableList<CartItem>,
) :
    RecyclerView.Adapter<MyCartRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: CartItem) -> Unit =
        { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = CartListRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        cartList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: CartListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: CartItem,
            position: Int,
            onItemSelected: (position: Int, item: CartItem) -> Unit
        ) {

            binding.cartModel = item
            binding.cardViewCartListRow.setOnClickListener {
                onItemSelected.invoke(position, item)
            }
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<CartItem>,
    ) {
        this.cartList = newDataSource
        notifyDataSetChanged()
    }

    fun gatherMyCartList(): MutableList<CartItem> {
        return this.cartList
    }

}
