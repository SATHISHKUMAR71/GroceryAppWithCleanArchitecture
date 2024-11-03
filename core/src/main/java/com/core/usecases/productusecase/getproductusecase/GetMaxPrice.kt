package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository

class GetMaxPrice(private var productRepository: ProductRepository) {
    fun invoke():Float{
        return productRepository.getMaxPriceInInventory()
    }
}