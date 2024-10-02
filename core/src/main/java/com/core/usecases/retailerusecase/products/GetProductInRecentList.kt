package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.recentlyvieweditems.RecentlyViewedItems

class GetProductInRecentList(private val retailerRepository: RetailerRepository) {
    fun invoke(productId:Long,userId:Int):RecentlyViewedItems?{
        return retailerRepository.getProductsInRecentList(productId,userId)
    }
}