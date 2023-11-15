package com.hms.referenceapp.hishopping.shoplocations.presentation

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alperenbabagil.simpleanimationpopuplibrary.SapDialog
import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.referenceapp.hishopping.shoplocations.R
import com.hms.referenceapp.hishopping.shoplocations.databinding.ItemRecyclerviewStoresBinding
import com.hms.referenceapp.hishopping.shoplocations.presentation.model.StoreItem

class StoreRecyclerViewAdapter : RecyclerView.Adapter<StoreRecyclerViewAdapter.StoreViewHolder>() {

    inner class StoreViewHolder(val binding: ItemRecyclerviewStoresBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<StoreItem>(){
        override fun areItemsTheSame(oldItem: StoreItem, newItem: StoreItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoreItem, newItem: StoreItem): Boolean {
            return oldItem == newItem
        }
    }

    val  differ = AsyncListDiffer(this,differCallback)
    private var onItemClickListener : ((StoreItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        return StoreViewHolder(ItemRecyclerviewStoresBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val storeItem = differ.currentList[position]
        holder.apply {
            binding.textViewStoresShopLocationTitle.text = storeItem.name
            binding.textViewStoresShopLocationDistance.text = storeItem.distanceText
            binding.buttonStoresNavigate.setOnClickListener {
                val res = NavigationAppOpener(itemView.context).open(
                    CommonLatLng(
                        storeItem.lat,
                        storeItem.lon
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
                onItemClickListener?.let { it(storeItem) }
            }
        }
    }

    fun setOnItemClickListener(listener: (StoreItem) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}