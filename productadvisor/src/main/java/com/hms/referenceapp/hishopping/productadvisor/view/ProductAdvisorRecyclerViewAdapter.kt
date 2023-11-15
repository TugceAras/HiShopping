package com.hms.referenceapp.hishopping.productadvisor.view

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hms.referenceapp.hishopping.data.model.Product
import com.hms.referenceapp.hishopping.productadvisor.R
import com.hms.referenceapp.hishopping.productadvisor.databinding.ItemRecyclerviewProductAdvisorBinding


class ProductAdvisorRecyclerViewAdapter(
    private val orientationType: Int,
    private var productList: MutableList<Product>
) :
    RecyclerView.Adapter<ProductAdvisorRecyclerViewAdapter.MainRecyclerViewHolder>() {

    private val productTypeList: HashMap<String, String> = hashMapOf()

    init {
        productTypeList["sport"] = "Behavior"
        productTypeList["headphone"] = "Headset"
        productTypeList["casual"] = "Time"
        productTypeList["former"] = "former"
        productTypeList["winter"] = "Weather"
        productTypeList["spring"] = "Weather"
        productTypeList["spring"] = "Weather"
        productTypeList["office"] = "Behavior"
        productTypeList["car"] = "Behavior"
    }

    internal var onItemSelected: (productId: Int, imageView: View, transitionName: String) -> Unit =
        { _, _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewProductAdvisorBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        productList[position].let {
            holder.bindItems(
                holder,
                it,
                position,
                onItemSelected,
                orientationType,
                productTypeList
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewProductAdvisorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            holder: MainRecyclerViewHolder,
            item: Product,
            position: Int,
            onItemSelected: (productId: Int, imageView: View, transitionName: String) -> Unit,
            orientationType: Int,
            productTypeList: HashMap<String, String>
        ) {
            if (orientationType == 1) {
                binding.imageViewProductAdvisor.layoutParams.width = 700
            } else {
                (binding.cardViewProductAdvisor.rootView.context as? Activity)?.let {
                    binding.imageViewProductAdvisor.layoutParams.width = getScreenWidth(it)
                }
            }
            binding.imageViewProductAdvisor.tag = "P_A_${item.id}"
            binding.imageViewProductAdvisor.load(item.image[0])
            binding.imageViewProductAdvisor.transitionName =
                binding.imageViewProductAdvisor.tag.toString()
            binding.textViewProductAdvisorDescription.text = item.title
            binding.textViewProductAdvisorPrice.text = holder.itemView.context.resources.getString(
                R.string.product_advisor_price,
                item.price.toString(),
                "$"
            )
            binding.textViewProductAdvisorType.text =
                if (productTypeList.containsKey(item.category)) {
                    productTypeList[item.category]
                } else {
                    ""
                }
            binding.cardViewProductAdvisor.setOnClickListener {
                onItemSelected.invoke(
                    item.id,
                    binding.imageViewProductAdvisor,
                    binding.imageViewProductAdvisor.tag.toString()
                )
            }
        }

        private fun getScreenWidth(activity: Activity): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                val insets: Insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.width() - insets.left - insets.right
            } else {
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<Product>,
    ) {
        this.productList = newDataSource
        notifyDataSetChanged()
    }


}

