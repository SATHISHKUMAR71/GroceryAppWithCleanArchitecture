package com.core.usecases.userusecase

import com.core.data.repository.UserRepository

class GetChildNames(private val userRepository: UserRepository) {
    fun invoke(parentName:String): List<String> {
        return userRepository.getChildName(parentName)
    }
}