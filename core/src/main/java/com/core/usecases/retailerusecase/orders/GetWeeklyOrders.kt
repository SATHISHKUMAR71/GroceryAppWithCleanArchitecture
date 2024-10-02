package com.core.usecases.retailerusecase.orders

import com.core.data.repository.RetailerRepository
import com.core.domain.order.OrderDetails

class GetWeeklyOrders(private var retailerRepository: RetailerRepository) {
    fun invoke(): List<OrderDetails>? {
        return retailerRepository.getOrdersForRetailerWeeklySubscription()
    }
}