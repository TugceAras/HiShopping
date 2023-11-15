// See the License for the specific language governing permissions and
// limitations under the License.
//
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.referenceapp.hishopping.app.myorders.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.myorders.domain.model.OrderViewItem
import com.hms.referenceapp.hishopping.databinding.OrderListRowBinding

class MyOrdersAdapter(
    private val itemLongClickCallback: ((orderViewItem: OrderViewItem) -> Unit)? = null,
    private val itemClickCallback: (orderViewItem: OrderViewItem) -> Unit
) :
    RecyclerView.Adapter<MyOrdersAdapter.OrderViewHolder>() {

    private val list = mutableListOf<OrderViewItem>()

    fun setItems(items: List<OrderViewItem>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }


    class OrderViewHolder(val binding: OrderListRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding: OrderListRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.order_list_row, parent, false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        //holder.binding.model = list[position]
        holder.itemView.setOnClickListener {
            itemClickCallback.invoke(list[position])
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickCallback?.invoke(list[position])
            true
        }
    }

    override fun getItemCount(): Int = list.size

}