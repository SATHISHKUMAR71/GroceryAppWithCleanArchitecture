package com.core.usecases.retailerusecase.orders

import com.core.data.repository.RetailerRepository
import com.core.domain.order.OrderDetails

class GetMonthlyOrders(private var retailerRepository: RetailerRepository) {
    fun invoke(): List<OrderDetails>? {
        return retailerRepository.getOrdersForRetailerMonthlySubscription()
    }
}