package com.assignment.cred.ui.category.fragments

import ParentAdapter
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.assignment.cred.R
import com.assignment.cred.databinding.FragmentCategoryBinding
import com.assignment.cred.models.ChildItem
import com.assignment.cred.models.ParentItem
import com.assignment.cred.utils.CategoryUiState
import com.assignment.cred.ui.category.viewmodels.CategoryViewModel
import com.assignment.cred.utils.Network
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryViewModel: CategoryViewModel

    private var parentList: List<ParentItem> = emptyList()

    private lateinit var parentAdapter: ParentAdapter
    private var mLayoutManager: GridLayoutManager? = null

    private var show = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        mLayoutManager = GridLayoutManager(requireContext(), 3)

        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(context)
            parentAdapter = ParentAdapter(parentList, mLayoutManager)
            parentAdapter.onParentItemClick = {
                val bundle = Bundle()
                bundle.putParcelable("CHILD_ITEM", it)
                findNavController().navigate(R.id.action_categoryFragment_to_homeFragment, bundle)
            }
            adapter = parentAdapter
        }

        if(!Network.isConnected(requireContext())) {
            Toast.makeText(requireContext(),"No Internet Connection",Toast.LENGTH_SHORT).show()
            return binding.root
        }

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                if (mLayoutManager?.spanCount == 1) {
                    mLayoutManager?.spanCount = 3
                }
            } else {
                mLayoutManager?.spanCount = 1
            }
            parentAdapter.notifyItemRangeChanged(0, parentAdapter.itemCount)
        }

        binding.button.setOnClickListener {
            val transition: Transition = Slide(Gravity.BOTTOM)
            transition.duration = 600
            transition.addTarget(binding.rvCategory)

            TransitionManager.beginDelayedTransition(binding.root, transition)
            binding.rvCategory.visibility = if (show) View.VISIBLE else View.GONE
            show = !show
            val drawableRes = if (show) {
                R.drawable.ic_baseline_keyboard_arrow_up_24
            } else {
                R.drawable.ic_baseline_keyboard_arrow_down_24
            }
            binding.button.setIconResource(drawableRes)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel.fetchCategories()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.categoryUiState.collect {
                    when (it) {
                        is CategoryUiState.CategoryList -> {
                            binding.progressBar.visibility = View.GONE
                            val groupedChildItems = it.list.groupBy { categoryItem ->
                                categoryItem.title
                            }
                            val parentItems = groupedChildItems.map { (title, items) ->
                                val childItems = items.map { categoryItem ->
                                    ChildItem(
                                        categoryItem.item.display_data.name,
                                        categoryItem.item.display_data.description
                                    )
                                }
                                ParentItem(title, childItems)
                            }
                            parentList = parentItems
                            parentAdapter.updateData(parentList)
                        }
                        CategoryUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        CategoryUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}