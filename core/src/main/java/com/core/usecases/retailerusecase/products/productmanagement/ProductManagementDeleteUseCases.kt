package com.core.usecases.retailerusecase.products.productmanagement

import com.core.usecases.retailerusecase.products.DeleteProduct
import com.core.usecases.retailerusecase.products.DeleteProductImage

data class ProductManagementDeleteUseCases(var mDeleteProductImage: DeleteProductImage, var mDeleteProduct: DeleteProduct)