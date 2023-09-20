package com.assignment.cred.di

import com.assignment.cred.api.CategoryService
import com.assignment.cred.repository.CategoryRepository
import com.assignment.cred.repository.CategoryRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesCategoryRepository(categoryService: CategoryService): CategoryRepository {
        return CategoryRepositoryImp(categoryService)
    }

}