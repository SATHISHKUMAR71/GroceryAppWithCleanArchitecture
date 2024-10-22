package com.core.usecases.productusecase.retailerproductusecase.getretailerproduct

import com.core.data.repository.ProductRepository
import com.core.domain.user.UserInfoWithOrderInfo

class GetUserInfoForModifiedProduct(private var productRepository: ProductRepository) {
    fun invoke(productId: Long): List<UserInfoWithOrderInfo>{
        return productRepository.getOrderInfoForSpecificProduct(productId)
    }
}