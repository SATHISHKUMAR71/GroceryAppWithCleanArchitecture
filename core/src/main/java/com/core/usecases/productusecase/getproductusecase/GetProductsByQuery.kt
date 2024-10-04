package com.core.usecases.productusecase.getproductusecase

import com.core.data.repository.ProductRepository

class GetProductsByQuery(private val productRepository: ProductRepository){
    fun invoke(query:String):List<String>?{
        return productRepository.getProductsForQuery(query)
    }
}