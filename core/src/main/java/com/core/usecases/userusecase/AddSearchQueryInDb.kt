package com.core.usecases.userusecase

import com.core.data.repository.SearchRepository
import com.core.data.repository.UserRepository
import com.core.domain.search.SearchHistory

class AddSearchQueryInDb(private var searchRepository: SearchRepository){
    operator fun invoke(searchHistory: SearchHistory){
        searchRepository.addSearchQueryInDb(searchHistory)
    }
}