package com.assignment.cred.repository

import android.util.Log
import com.assignment.cred.api.CategoryService
import com.assignment.cred.models.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepositoryImp @Inject constructor(private val service: CategoryService) :
    CategoryRepository {
    override suspend fun getCategories(): Flow<List<Item>> {
        return flow {
            emit(service.getCategories().data.flatMap {
                it.section_properties.items.map { item ->
                    Item(
                        id = item.id,
                        display_data = item.display_data
                    )
                }
            })
        }.catch { exception ->
            Log.d("REPOSITORY_IMP", "ERROR HERE")
            exception.printStackTrace()
        }
    }
}