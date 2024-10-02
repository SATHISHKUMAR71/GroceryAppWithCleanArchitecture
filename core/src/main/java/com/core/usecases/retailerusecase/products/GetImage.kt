package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.Images

class GetImage(private val retailerRepository: RetailerRepository) {
    fun invoke(image:String):Images?{
        return retailerRepository.getSpecificImage(image)
    }
}