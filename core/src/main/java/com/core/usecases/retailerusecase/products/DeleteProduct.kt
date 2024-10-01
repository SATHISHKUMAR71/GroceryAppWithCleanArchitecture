package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.Product

class DeleteProduct(private val retailerRepository: RetailerRepository) {
    fun invoke(product: Product){
        retailerRepository.deleteProduct(product)
    }
}