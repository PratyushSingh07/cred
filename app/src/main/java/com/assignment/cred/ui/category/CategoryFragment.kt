package com.assignment.cred.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.assignment.cred.databinding.FragmentCategoryBinding
import com.assignment.cred.utils.CategoryUiState
import com.assignment.cred.viewmodels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
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
                        }
                        CategoryUiState.Error -> {

                        }
                        CategoryUiState.Loading -> {

                        }
                    }
                }
            }
        }

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Switch On", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Switch Off", Toast.LENGTH_SHORT).show()
            }
        }
    }
}