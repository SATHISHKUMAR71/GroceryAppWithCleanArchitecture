package com.core.usecases.userusecase

import com.core.data.repository.SearchRepository
import com.core.data.repository.UserRepository

class PerformCategorySearch(private var searchRepository: SearchRepository) {
    operator fun invoke(query:String):List<String>?{
        return searchRepository.getProductsForQuery(query)
    }
}