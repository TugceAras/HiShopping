package com.hms.referenceapp.hishopping.app.myorders.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hms.referenceapp.hishopping.data.model.Favorites
import com.hms.referenceapp.hishopping.data.model.OrderEntity
import com.hms.referenceapp.hishopping.databinding.OrderListRowBinding

class OrdersRecyclerViewAdapter : RecyclerView.Adapter<OrdersRecyclerViewAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(val binding: OrderListRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<OrderEntity>(){
        override fun areItemsTheSame(oldItem: OrderEntity, newItem: OrderEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderEntity, newItem: OrderEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
    private var onItemClickListener : ((OrderEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            OrderListRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val orderEntity = differ.currentList[position]
        holder.apply {
            binding.orderEntity = orderEntity
            binding.cardViewOrderListRow.setOnClickListener {
                onItemClickListener?.let {
                    it(orderEntity)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (OrderEntity) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}