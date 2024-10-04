package com.core.usecases.searchusecase

import com.core.data.repository.SearchRepository

class PerformCategorySearch(private var searchRepository: SearchRepository) {
    operator fun invoke(query:String):List<String>?{
        return searchRepository.getProductsForQuery(query)
    }
}