package com.core.data.datasource.subscriptiondatasource

import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce

interface GetSubscriptionDataSource {
    fun getOrderedDayForWeekSubscription(orderId:Int): WeeklyOnce?
    fun getOrderForDailySubscription(orderId:Int): DailySubscription?
    fun getOrderForMonthlySubscription(orderId:Int): MonthlyOnce?
    fun getOrderedTimeSlot(orderId:Int): TimeSlot?
    fun getMonthlySubscriptionAndTimeSlot(orderId: Int):Map<MonthlyOnce,TimeSlot>
    fun getWeeklySubscriptionAndTimeSlot(orderId: Int):Map<WeeklyOnce,TimeSlot>
    fun getDailySubscriptionAndTimeSlot(orderId: Int):Map<DailySubscription,TimeSlot>
}