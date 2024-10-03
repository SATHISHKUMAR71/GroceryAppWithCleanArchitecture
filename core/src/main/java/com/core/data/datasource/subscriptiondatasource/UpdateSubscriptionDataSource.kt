package com.core.data.datasource.subscriptiondatasource

import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce

interface UpdateSubscriptionDataSource {
    fun updateTimeSlot(timeSlot: TimeSlot)
    fun deleteFromWeeklySubscription(weeklyOnce: WeeklyOnce)
    fun deleteFromMonthlySubscription(monthlyOnce: MonthlyOnce)
    fun deleteFromDailySubscription(dailySubscription: DailySubscription)
}