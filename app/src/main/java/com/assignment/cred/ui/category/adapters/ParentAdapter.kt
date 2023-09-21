package com.assignment.cred.ui.category.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.cred.R
import com.assignment.cred.models.ParentItem

class ParentAdapter(
    private var parentList: List<ParentItem>,
    private val layoutManager: GridLayoutManager? = null,
) :
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    inner class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.findViewById(R.id.parentTitleTv)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.langRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parent, parent, false)
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val parentItem = parentList[position]
        holder.titleTv.text = parentItem.title

        var spanCount = if (layoutManager?.spanCount == 3) {
            4
        } else {
            1
        }
        holder.childRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, spanCount)
        val adapter = ChildAdapter(
            parentItem.mList,
            layoutManager
        )
        holder.childRecyclerView.adapter = adapter
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    fun updateData(parentItemList: List<ParentItem>) {
        this.parentList = parentItemList
        notifyDataSetChanged()
    }
}