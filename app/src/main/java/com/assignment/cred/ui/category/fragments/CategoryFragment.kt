package com.assignment.cred.ui.category.fragments

import ChildAdapter
import ParentAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.cred.R
import com.assignment.cred.databinding.FragmentCategoryBinding
import com.assignment.cred.models.ChildItem
import com.assignment.cred.models.ParentItem
import com.assignment.cred.utils.CategoryUiState
import com.assignment.cred.viewmodels.CategoryViewModel
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
                bundle.putParcelable("CHILD_ITEM",it)
//                val action = CategoryFragmentDirections.actionCategoryFragmentToHomeFragment(it)
                findNavController().navigate(R.id.action_categoryFragment_to_homeFragment,bundle)
            }
            adapter = parentAdapter
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
                            Log.d("CATEGORY", it.list.toString())
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

                        }
                        CategoryUiState.Loading -> {

                        }
                    }
                }
            }
        }
    }
}