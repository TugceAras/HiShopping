package com.hms.referenceapp.hishopping.mycreditcards.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hms.referenceapp.hishopping.data.model.CreditCard
import com.hms.referenceapp.hishopping.mycreditcards.databinding.ItemRecyclerviewCreditCardRowBinding


class MyCreditCardsRecyclerViewAdapter(
    private var creditCardList: MutableList<CreditCard>
) :
    RecyclerView.Adapter<MyCreditCardsRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: CreditCard) -> Unit =
        { _, _ -> }

    internal var onItemLongSelected: (position: Int, item: CreditCard) -> Unit =
        { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewCreditCardRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return creditCardList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        creditCardList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected,
                onItemLongSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewCreditCardRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: CreditCard,
            position: Int,
            onItemSelected: (position: Int, item: CreditCard) -> Unit,
            onItemLongSelected: (position: Int, item: CreditCard) -> Unit
        ) {
            binding.model = item
            binding.creditCardView.setOnClickListener {
                onItemSelected.invoke(position, item)
            }
            binding.creditCardView.setOnLongClickListener {
                onItemLongSelected.invoke(position, item)
                true
            }
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<CreditCard>,
    ) {
        this.creditCardList = newDataSource
        notifyDataSetChanged()
    }


}

