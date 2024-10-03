package com.core.usecases.userusecase

import com.core.data.repository.UserRepository
import com.core.domain.user.User

class UpdateExistingUser (private val userRepository: UserRepository){
    fun invoke(user: User){
        userRepository.updateExistingUser(user)
    }
}