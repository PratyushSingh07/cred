package com.assignment.cred.utils

import com.assignment.cred.models.CategoryUi

sealed class CategoryUiState {
    object Loading : CategoryUiState()
    object Error : CategoryUiState()
    data class CategoryList(val list: List<CategoryUi>) : CategoryUiState()
}