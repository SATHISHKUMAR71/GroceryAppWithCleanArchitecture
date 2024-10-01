package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.Product

class GetLastProduct(private val retailerRepository: RetailerRepository) {
    fun invoke():Product{
        return retailerRepository.getLastProduct()
    }
}