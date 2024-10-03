package com.core.usecases.userusecase

import com.core.data.repository.SearchRepository
import com.core.data.repository.UserRepository
import com.core.domain.search.SearchHistory

class GetSearchList(private var searchRepository: SearchRepository) {
    operator fun invoke(userId:Int):List<SearchHistory>?{
        return searchRepository.getSearchHistory(userId)
    }
}