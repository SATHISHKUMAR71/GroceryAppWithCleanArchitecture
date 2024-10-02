package com.core.usecases.customerusecase.products

import com.core.data.repository.CustomerRepository
import com.core.domain.products.Images

class GetImagesForProduct(private val customerRepository: CustomerRepository) {
    fun invoke(productId:Long):List<Images>?{
        return customerRepository.getImagesForProduct(productId)
    }
}