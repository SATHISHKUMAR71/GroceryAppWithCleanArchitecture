package com.core.usecases.searchusecase

import com.core.data.repository.SearchRepository
import com.core.domain.search.SearchHistory

class GetSearchList(private var searchRepository: SearchRepository) {
    operator fun invoke(userId:Int):List<SearchHistory>?{
        println("SEARCH LIST: ${searchRepository.getSearchHistory(userId)}")
        return searchRepository.getSearchHistory(userId)
    }
}