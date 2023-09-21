package com.assignment.cred.ui.category.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.cred.R
import com.assignment.cred.models.ChildItem
import com.assignment.cred.ui.category.enums.ViewType

class ChildAdapter(
    private val childList: List<ChildItem>,
    private val layoutManager: GridLayoutManager? = null,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.DETAILED.ordinal -> DetailedViewHolder(parent)
            else -> SimpleViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SimpleViewHolder -> {
                val item = childList[position]
                holder.itemView.findViewById<TextView>(R.id.tvCategoryNameGrid).text =
                    item.title
            }

            is DetailedViewHolder -> {
                val item = childList[position]
                holder.itemView.findViewById<TextView>(R.id.tvCategoryName).text =
                    item.title
                holder.itemView.findViewById<TextView>(R.id.tvCategorySubtitleName).text =
                    item.subtitle
            }
        }
    }

    override fun getItemCount(): Int {
        return childList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager?.spanCount == 1) ViewType.DETAILED.ordinal
        else ViewType.SMALL.ordinal
    }

    inner class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(parent: ViewGroup)
                : this(
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        )
    }

    inner class DetailedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        constructor(parent: ViewGroup)
                : this(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        )
    }

}