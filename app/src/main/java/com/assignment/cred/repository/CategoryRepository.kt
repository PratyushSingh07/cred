package com.assignment.cred.repository

import com.assignment.cred.models.Item
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategories(): Flow<List<Item>>

}