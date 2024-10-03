package com.core.usecases.customerusecase.orders

import com.core.data.repository.OrderRepository
import com.core.domain.order.Cart
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData

class GetOrderWithProductsByOrderId(private val orderRepository: OrderRepository)  {
    fun invoke(orderId:Int):Map<OrderDetails,List<CartWithProductData>>?{
        return orderRepository.getOrderWithProductsWithOrderId(orderId)
    }
}