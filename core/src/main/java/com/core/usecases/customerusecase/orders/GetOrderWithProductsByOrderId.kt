package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.Cart
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData

class GetOrderWithProductsByOrderId(private val customerRepository: CustomerRepository)  {
    fun invoke(orderId:Int):Map<OrderDetails,List<CartWithProductData>>?{
        return customerRepository.getOrderWithProductsWithOrderId(orderId)
    }
}