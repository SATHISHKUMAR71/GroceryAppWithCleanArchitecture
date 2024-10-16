package com.core.usecases.productusecase.setproductusecase

import com.core.data.repository.ProductRepository
import com.core.domain.recentlyvieweditems.RecentlyViewedItems

class RemoveFromRecentlyViewedProducts(private val productRepository: ProductRepository) {
    fun invoke(recentlyViewedItems: RecentlyViewedItems) {
        productRepository.removeProductInRecentlyViewedItems(recentlyViewedItems)
    }
}