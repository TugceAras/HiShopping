package com.hms.referenceapp.hishopping.app.favorites.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.data.model.Favorites
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewFavoritesBinding

class FavoritesRecyclerViewAdapter : RecyclerView.Adapter<FavoritesRecyclerViewAdapter.FavoriteProductsViewHolder>() {

    inner class FavoriteProductsViewHolder(val binding: ItemRecyclerviewFavoritesBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Favorites>(){
        override fun areItemsTheSame(oldItem: Favorites, newItem: Favorites): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Favorites, newItem: Favorites): Boolean {
            return oldItem == newItem
        }
    }

    val  differ = AsyncListDiffer(this,differCallback)
    private var onItemClickListener : ((Favorites) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductsViewHolder {
        return FavoriteProductsViewHolder(ItemRecyclerviewFavoritesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: FavoriteProductsViewHolder, position: Int) {
        val favorites = differ.currentList[position]
        holder.binding.favorites = favorites
        holder.binding.imageViewFavoriteList.load(favorites.productPhotoUrl)
        holder.binding.imageViewFavoriteButton.setOnClickListener {
            onItemClickListener?.let { it(favorites) }
        }
    }

    fun setOnItemClickListener(listener: (Favorites) -> Unit){
        onItemClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}