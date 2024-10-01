package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.Product

class UpdateProduct(private val retailerRepository: RetailerRepository) {
    fun invoke(product: Product){
        retailerRepository.updateProduct(product)
    }
}