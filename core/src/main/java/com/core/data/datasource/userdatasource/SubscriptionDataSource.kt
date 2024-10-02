package com.core.data.datasource.userdatasource

import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce

interface SubscriptionDataSource {
    fun updateTimeSlot(timeSlot: TimeSlot)
    fun deleteFromWeeklySubscription(weeklyOnce: WeeklyOnce)
    fun deleteFromMonthlySubscription(monthlyOnce: MonthlyOnce)
    fun deleteFromDailySubscription(dailySubscription: DailySubscription)
    fun getDailySubscription():List<DailySubscription>?
    fun getOrderTimeSlot():List<TimeSlot>?
    fun getWeeklySubscriptionList():List<WeeklyOnce>?
    fun getMonthlySubscriptionList():List<MonthlyOnce>?
    fun getOrderedDayForWeekSubscription(orderId:Int):WeeklyOnce?
    fun getOrderForDailySubscription(orderId:Int):DailySubscription?
    fun getOrderForMonthlySubscription(orderId:Int):MonthlyOnce?
    fun getOrderedTimeSlot(orderId:Int):TimeSlot?
}