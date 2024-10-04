package com.core.usecases.searchusecase

import com.core.data.repository.SearchRepository
import com.core.domain.search.SearchHistory

class AddSearchQueryInDb(private var searchRepository: SearchRepository){
    operator fun invoke(searchHistory: SearchHistory){
        searchRepository.addSearchQueryInDb(searchHistory)
    }
}