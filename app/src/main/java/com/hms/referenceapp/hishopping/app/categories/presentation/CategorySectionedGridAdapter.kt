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
package com.hms.referenceapp.hishopping.app.categories.presentation

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import java.util.*

class CategorySectionedGridAdapter(
    private val context: Context,
    private val sectionResourceId: Int,
    private val textResourceId: Int,
    private val recyclerView: RecyclerView,
    private val baseAdapter:   RecyclerView.Adapter<CategoryProductsRecyclerViewAdapter.MainRecyclerViewHolder>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mValid = true
    private val mSections = SparseArray<Section?>()

    class SectionViewHolder(view: View, mTextResourceId: Int) :
        RecyclerView.ViewHolder(view) {
        var title = view.findViewById<View>(mTextResourceId) as TextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        typeView: Int
    ): RecyclerView.ViewHolder {
        return if (typeView == SECTION_TYPE) {
            val view =
                LayoutInflater.from(context).inflate(sectionResourceId, parent, false)
            SectionViewHolder(
                view,
                textResourceId
            )
        } else {
            baseAdapter.onCreateViewHolder(parent, typeView - 1)
        }
    }

    override fun onBindViewHolder(
        sectionViewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (isSectionHeaderPosition(position)) {
            (sectionViewHolder as SectionViewHolder).title.text = mSections[position]!!.title
        }
        else{
            baseAdapter.onBindViewHolder(sectionViewHolder as CategoryProductsRecyclerViewAdapter.MainRecyclerViewHolder,
                sectionedPositionToPosition(position));
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (isSectionHeaderPosition(position)) SECTION_TYPE else baseAdapter.getItemViewType(
            sectionedPositionToPosition(position)
        ) + 1
    }

    class Section(var firstPosition: Int, var title: CharSequence) {
        var sectionedPosition = 0
    }

    fun setSections(sections: List<Section>) {
        mSections.clear()
        Arrays.sort(
            sections.toTypedArray()
        ) { o, o1 -> if (o.firstPosition == o1.firstPosition) 0 else if (o.firstPosition < o1.firstPosition) -1 else 1 }
        for ((offset, section) in sections.withIndex()) {
            section.sectionedPosition = section.firstPosition + offset
            mSections.append(section.sectionedPosition, section)
        }
        notifyDataSetChanged()
    }

    fun positionToSectionedPosition(position: Int): Int {
        var offset = 0
        for (i in 0 until mSections.size()) {
            if (mSections.valueAt(i)!!.firstPosition > position) {
                break
            }
            ++offset
        }
        return position + offset
    }

    private fun sectionedPositionToPosition(sectionedPosition: Int): Int {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION
        }
        var offset = 0
        for (i in 0 until mSections.size()) {
            if (mSections.valueAt(i)!!.sectionedPosition > sectionedPosition) {
                break
            }
            --offset
        }
        return sectionedPosition + offset
    }

    fun isSectionHeaderPosition(position: Int): Boolean {
        return mSections[position] != null
    }

    override fun getItemId(position: Int): Long {
        return if (isSectionHeaderPosition(position)) (Int.MAX_VALUE - mSections.indexOfKey(
            position
        )).toLong() else baseAdapter.getItemId(sectionedPositionToPosition(position))
    }

    override fun getItemCount(): Int {
        return if (mValid) baseAdapter.itemCount + mSections.size() else 0
    }

    companion object {
        private const val SECTION_TYPE = 0
    }

    init {
        baseAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                mValid = baseAdapter.itemCount > 0
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(
                positionStart: Int,
                itemCount: Int
            ) {
                mValid = baseAdapter.itemCount > 0
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(
                positionStart: Int,
                itemCount: Int
            ) {
                mValid = baseAdapter.itemCount > 0
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeRemoved(
                positionStart: Int,
                itemCount: Int
            ) {
                mValid = baseAdapter.itemCount > 0
                notifyItemRangeRemoved(positionStart, itemCount)
            }
        })

        (recyclerView.layoutManager as? GridLayoutManager)?.let {
            it.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isSectionHeaderPosition(position)) it.spanCount else 1
                }
            }
        }
    }

}