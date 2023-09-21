import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.cred.databinding.ItemGridBinding
import com.assignment.cred.databinding.ItemListBinding
import com.assignment.cred.models.ChildItem
import com.assignment.cred.ui.category.enums.ViewType

class ChildAdapter(
    private val childList: List<ChildItem>,
    private val layoutManager: GridLayoutManager? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.DETAILED.ordinal -> {
                val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DetailedViewHolder(binding)
            }
            else -> {
                val binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SimpleViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = childList[position]
        when (holder) {
            is SimpleViewHolder -> {
                holder.bind(item)
            }

            is DetailedViewHolder -> {
                holder.bind(item)
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

    inner class SimpleViewHolder(private val binding: ItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChildItem) {
            binding.tvCategoryNameGrid.text = item.title
        }
    }

    inner class DetailedViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChildItem) {
            binding.tvCategoryName.text = item.title
            binding.tvCategorySubtitleName.text = item.subtitle
        }
    }
}
