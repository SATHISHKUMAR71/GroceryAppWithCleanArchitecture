package com.core.usecases.customerusecase.orders

import com.core.data.repository.CustomerRepository
import com.core.domain.order.TimeSlot

class AddTimeSlot(private val customerRepository: CustomerRepository) {
    fun invoke(timeSlot: TimeSlot){
        customerRepository.addTimeSlot(timeSlot)
    }
}