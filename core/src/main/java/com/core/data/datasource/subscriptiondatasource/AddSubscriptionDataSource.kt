package com.core.data.datasource.subscriptiondatasource

import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce

interface AddSubscriptionDataSource {
    fun addTimeSlot(timeSlot: TimeSlot)
    fun addMonthlyOnceSubscription(monthlyOnce: MonthlyOnce)
    fun addWeeklyOnceSubscription(weeklyOnce: WeeklyOnce)
    fun addDailySubscription(dailySubscription: DailySubscription)
}