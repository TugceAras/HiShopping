package com.hms.referenceapp.hishopping.address.presentation.addresses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hms.referenceapp.hishopping.address.databinding.ItemAddressBinding
import com.hms.referenceapp.hishopping.data.model.UserAddress


class AddressRecyclerViewAdapter(
    private var userAddressList: MutableList<UserAddress>,
) :
    RecyclerView.Adapter<AddressRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, userAddress: UserAddress) -> Unit =
        { _, _ -> }

    internal var onItemDeleted: (position: Int, userAddress: UserAddress) -> Unit =
        { _, _ -> }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemAddressBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return userAddressList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        userAddressList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected,
                onItemDeleted
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: UserAddress,
            position: Int,
            onItemSelected: (position: Int, userAddress: UserAddress) -> Unit,
            onItemDeleted: (position: Int, userAddress: UserAddress) -> Unit,
        ) {
            binding.textViewAddressTitle.text = item.title
            binding.textViewAddressTitle.setOnClickListener {
                onItemSelected.invoke(position, item)
            }
            binding.imageViewAddressDelete.setOnClickListener {
                onItemDeleted.invoke(position, item)
            }
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<UserAddress>,
    ) {
        this.userAddressList = newDataSource
        notifyDataSetChanged()
    }

}
