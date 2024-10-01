package com.core.usecases.userusecase

import com.core.data.repository.UserRepository
import com.core.domain.search.SearchHistory

class GetSearchList(private var userRepository: UserRepository) {
    operator fun invoke(userId:Int):List<SearchHistory>{
        return userRepository.getSearchHistory(userId)
    }
}