package com.assignment.cred.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.cred.repository.CategoryRepository
import com.assignment.cred.utils.CategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: CategoryRepository) :
    ViewModel() {

    private var _categoryUiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Loading)
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState


    fun fetchCategories() {
        viewModelScope.launch {
            _categoryUiState.value = CategoryUiState.Loading
            repository.getCategories().catch {
                Log.d("ERROR",repository.getCategories().first().toString())
                _categoryUiState.value = CategoryUiState.Error
            }.collect {
                _categoryUiState.value = CategoryUiState.CategoryList(it)
            }
        }
    }

}