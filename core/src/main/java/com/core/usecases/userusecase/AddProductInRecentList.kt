package com.core.usecases.userusecase

import com.core.data.repository.ProductRepository
import com.core.data.repository.UserRepository
import com.core.domain.recentlyvieweditems.RecentlyViewedItems

class AddProductInRecentList(private val productRepository: ProductRepository) {
    fun invoke(recentlyViewedItems: RecentlyViewedItems){
        productRepository.addProductInRecentlyViewedItems(recentlyViewedItems)
    }
}