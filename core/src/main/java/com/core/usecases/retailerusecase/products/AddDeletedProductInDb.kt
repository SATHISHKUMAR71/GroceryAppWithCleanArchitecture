package com.core.usecases.retailerusecase.products

import com.core.data.repository.ProductRepository
import com.core.domain.products.DeletedProductList
import com.core.domain.products.Product

class AddDeletedProductInDb(private val productRepository: ProductRepository) {
    fun invoke(deletedProductList: DeletedProductList){
        productRepository.addDeletedProduct(deletedProductList)
    }
}