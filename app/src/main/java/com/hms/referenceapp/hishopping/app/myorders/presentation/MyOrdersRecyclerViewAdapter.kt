package com.hms.referenceapp.hishopping.app.myorders.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hms.referenceapp.hishopping.app.myorders.domain.model.OrderViewItem
import com.hms.referenceapp.hishopping.databinding.OrderListRowBinding


class MyOrdersRecyclerViewAdapter(
    private var orderViewItemList: MutableList<OrderViewItem>,
) :
    RecyclerView.Adapter<MyOrdersRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: OrderViewItem) -> Unit =
        { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = OrderListRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return orderViewItemList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        orderViewItemList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: OrderListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: OrderViewItem,
            position: Int,
            onItemSelected: (position: Int, item: OrderViewItem) -> Unit
        ) {
            //binding.model = item
            binding.cardViewOrderListRow.setOnClickListener {
                onItemSelected.invoke(position, item)
            }
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<OrderViewItem>,
    ) {
        this.orderViewItemList = newDataSource
        notifyDataSetChanged()
    }

}
