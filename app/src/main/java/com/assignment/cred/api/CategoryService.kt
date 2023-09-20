package com.assignment.cred.api

import com.assignment.cred.models.CategoryResponse
import retrofit2.http.GET

interface CategoryService {

    @GET("p68785/skuSections")
    suspend fun getCategories(): CategoryResponse

}