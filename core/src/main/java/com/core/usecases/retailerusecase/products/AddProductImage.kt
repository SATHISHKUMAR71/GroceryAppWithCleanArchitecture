package com.core.usecases.retailerusecase.products

import com.core.data.repository.RetailerRepository
import com.core.domain.products.Images

class AddProductImage(private val retailerRepository: RetailerRepository){
    fun invoke(images: Images){
        retailerRepository.addProductImagesInDb(images)
    }
}