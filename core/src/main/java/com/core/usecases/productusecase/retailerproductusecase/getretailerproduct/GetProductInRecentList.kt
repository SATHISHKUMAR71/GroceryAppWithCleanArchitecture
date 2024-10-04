package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.recentlyvieweditems.RecentlyViewedItems

class GetProductInRecentList(private val productRepository: ProductRepository) {
    fun invoke(productId:Long,userId:Int):RecentlyViewedItems?{
        return productRepository.getProductsInRecentList(productId,userId)
    }
}