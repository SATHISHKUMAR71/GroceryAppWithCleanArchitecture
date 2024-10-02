package com.core.usecases.retailerusecase.orders

import com.core.data.repository.RetailerRepository
import com.core.domain.order.OrderDetails

class GetNormalOrder(private var retailerRepository: RetailerRepository) {
    fun invoke(): List<OrderDetails>? {
        return retailerRepository.getOrdersForRetailerNoSubscription()
    }
}