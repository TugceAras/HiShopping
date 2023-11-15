package com.hms.referenceapp.hishopping.app.explore.campaigns.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.data.model.Campaign
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewCampaignsBinding


class CampaignsSliderAdapter(
    private var campaignList: MutableList<Campaign>,
) :
    RecyclerView.Adapter<CampaignsSliderAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: Campaign) -> Unit =
        { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewCampaignsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return campaignList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        campaignList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewCampaignsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: Campaign,
            position: Int,
            onItemSelected: (position: Int, item: Campaign) -> Unit
        ) {
            binding.imageViewCampaigns.load(
                getImageResource(
                    holder.itemView.rootView.context,
                    item.image
                )
            )

            binding.imageViewCampaigns.setOnClickListener {
                onItemSelected.invoke(position, item)
            }
        }

        private fun getImageResource(context: Context, image: String?): Int {
            return context.resources.getIdentifier(image!!, "mipmap", context.packageName)
        }

    }

    fun updateDataSource(
        newDataSource: MutableList<Campaign>,
    ) {
        this.campaignList = newDataSource
        notifyDataSetChanged()
    }


}
