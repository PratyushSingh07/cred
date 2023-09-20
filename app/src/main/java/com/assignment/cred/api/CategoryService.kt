package com.assignment.cred.api

import retrofit2.http.GET

interface CategoryService {

    @GET("p68785/skuSections")
    fun getCategories()

}