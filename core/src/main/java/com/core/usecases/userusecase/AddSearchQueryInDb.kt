package com.core.usecases.userusecase

import com.core.data.repository.UserRepository
import com.core.domain.search.SearchHistory

class AddSearchQueryInDb(private var userRepository: UserRepository){
    operator fun invoke(searchHistory: SearchHistory){
        userRepository.addSearchQueryInDb(searchHistory)
    }
}