package com.assignment.cred.utils

import com.assignment.cred.models.Item

sealed class CategoryUiState {
    object Loading : CategoryUiState()
    object Error : CategoryUiState()
    data class CategoryList(val list: List<Item>) : CategoryUiState()
}