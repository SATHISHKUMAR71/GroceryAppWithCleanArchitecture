package com.core.usecases.customerusecase.products

import com.core.data.repository.ProductRepository

class GetProductsQueryForName(private val productRepository: ProductRepository) {
    fun invoke(query:String):List<String>?{
        return productRepository.getProductForQueryName(query)
    }
}