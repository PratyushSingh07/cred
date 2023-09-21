package com.assignment.cred.ui.category.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.cred.databinding.FragmentCategoryBinding
import com.assignment.cred.models.ChildItem
import com.assignment.cred.models.ParentItem
import com.assignment.cred.ui.category.adapters.ParentAdapter
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
                            val childItems = ArrayList<ChildItem>()
                            it.list.forEach {
                                childItems.add(
                                    ChildItem(
                                        it.display_data.name,
                                        it.display_data.description
                                    )
                                )
                            }
                            parentList = listOf(
                                ParentItem("TITLE 1", childItems),
                                ParentItem("TITLE 2", childItems),
                                ParentItem("TITLE 3", childItems)
                            )
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