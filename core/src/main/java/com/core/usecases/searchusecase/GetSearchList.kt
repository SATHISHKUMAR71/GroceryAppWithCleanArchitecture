package com.core.usecases.searchusecase

import com.core.data.repository.SearchRepository
import com.core.domain.search.SearchHistory

class GetSearchList(private var searchRepository: SearchRepository) {
    operator fun invoke(userId:Int):List<SearchHistory>?{
        return searchRepository.getSearchHistory(userId)
    }
}