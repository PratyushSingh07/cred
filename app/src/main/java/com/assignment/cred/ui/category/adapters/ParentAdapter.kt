import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.cred.databinding.ItemParentBinding
import com.assignment.cred.models.ChildItem
import com.assignment.cred.models.ParentItem

class ParentAdapter(
    private var parentList: List<ParentItem>,
    private val layoutManager: GridLayoutManager? = null
) : RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    var onParentItemClick: ((ChildItem) -> Unit)? = null

    inner class ParentViewHolder(private val binding: ItemParentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(parentItem: ParentItem) {
            binding.parentTitleTv.text = parentItem.title

            var spanCount = if (layoutManager?.spanCount == 3) {
                4
            } else {
                1
            }
            binding.langRecyclerView.layoutManager =
                GridLayoutManager(binding.root.context, spanCount)
            val adapter = ChildAdapter(
                parentItem.mList,
                layoutManager
            )
            adapter.onItemClick = {
                onParentItemClick?.invoke(it)
                println("PARENT_CLICKED ${it.title}")
            }
            binding.langRecyclerView.adapter = adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding =
            ItemParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val parentItem = parentList[position]
        holder.bind(parentItem)
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    fun updateData(parentItemList: List<ParentItem>) {
        this.parentList = parentItemList
        notifyDataSetChanged()
    }
}
