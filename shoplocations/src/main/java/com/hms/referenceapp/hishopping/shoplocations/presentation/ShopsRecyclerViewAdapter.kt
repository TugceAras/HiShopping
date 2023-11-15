package com.hms.referenceapp.hishopping.shoplocations.presentation

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alperenbabagil.simpleanimationpopuplibrary.SapDialog
import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.referenceapp.hishopping.shoplocations.R
import com.hms.referenceapp.hishopping.shoplocations.databinding.ItemRecyclerviewStoresBinding
import com.hms.referenceapp.hishopping.shoplocations.presentation.model.ShopLocationListWrapper


class ShopsRecyclerViewAdapter(
    private var shopLocationListWrapperList: MutableList<ShopLocationListWrapper>,
) :
    RecyclerView.Adapter<ShopsRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: ShopLocationListWrapper) -> Unit =
        { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewStoresBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return shopLocationListWrapperList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        shopLocationListWrapperList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewStoresBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: ShopLocationListWrapper,
            position: Int,
            onItemSelected: (position: Int, Item: ShopLocationListWrapper) -> Unit
        ) {
            binding.textViewStoresShopLocationTitle.text = item.shopLocation.visibleName
            binding.textViewStoresShopLocationDistance.text = item.distanceString
            binding.buttonStoresNavigate.setOnClickListener {
                val res = NavigationAppOpener(itemView.context).open(
                    CommonLatLng(
                        item.shopLocation.location.first,
                        item.shopLocation.location.second
                    )
                )
                if (!res.opened) {
                    (itemView.context as? Activity)?.let {
                        SapDialog(it).apply {
                            titleText = it.getString(R.string.error)
                            messageText = res.exception?.message
                            animResource = R.raw.sap_error_anim
                            loopAnimation = false
                            isCancellable = true
                        }.build().show()
                    }
                }
            }

            binding.constraintLayoutStores.setOnClickListener {
                onItemSelected.invoke(position, item)
            }
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<ShopLocationListWrapper>,
    ) {
        this.shopLocationListWrapperList = newDataSource
        notifyDataSetChanged()
    }


}

