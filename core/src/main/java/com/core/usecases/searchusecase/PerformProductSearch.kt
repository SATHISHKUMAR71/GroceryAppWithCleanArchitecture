package com.core.usecases.searchusecase

import com.core.data.repository.SearchRepository

class PerformProductSearch(private val searchRepository: SearchRepository) {
    operator fun invoke(query:String):List<String>?{
        return searchRepository.getProductForQueryName(query)
    }
}