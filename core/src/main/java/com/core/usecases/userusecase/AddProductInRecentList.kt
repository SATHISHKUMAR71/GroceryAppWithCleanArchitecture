package com.core.usecases.userusecase

import com.core.data.repository.RetailerRepository
import com.core.data.repository.UserRepository
import com.core.domain.recentlyvieweditems.RecentlyViewedItems

class AddProductInRecentList(private val userRepository: UserRepository) {
    fun invoke(recentlyViewedItems: RecentlyViewedItems){
        userRepository.addProductInRecentlyViewedItems(recentlyViewedItems)
    }
}