package com.core.data.repository

import com.core.data.datasource.subscriptiondatasource.AddSubscriptionDataSource
import com.core.data.datasource.subscriptiondatasource.GetSubscriptionDataSource
import com.core.data.datasource.subscriptiondatasource.UpdateSubscriptionDataSource
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce

class SubscriptionRepository(private val addSubscriptionDataSource: AddSubscriptionDataSource,private val getSubscriptionDataSource: GetSubscriptionDataSource,val updateSubscriptionDataSource: UpdateSubscriptionDataSource){

    fun getOrderedDayForWeekSubscription(orderId:Int): WeeklyOnce?{
        return getSubscriptionDataSource.getOrderedDayForWeekSubscription(orderId)
    }

    fun getOrderedDayForMonthlySubscription(orderId:Int): MonthlyOnce?{
        return getSubscriptionDataSource.getOrderForMonthlySubscription(orderId)
    }
    fun getOrderForDailySubscription(orderId:Int): DailySubscription?{
        return getSubscriptionDataSource.getOrderForDailySubscription(orderId)
    }
    fun getOrderedTimeSlot(orderId:Int): TimeSlot?{
        return getSubscriptionDataSource.getOrderedTimeSlot(orderId)
    }
    fun updateTimeSlot(timeSlot: TimeSlot){
        updateSubscriptionDataSource.updateTimeSlot(timeSlot)
    }

    fun getMonthlySubscriptionWithTimeslot(orderId: Int): Map<MonthlyOnce, TimeSlot> {
        return getSubscriptionDataSource.getMonthlySubscriptionAndTimeSlot(orderId)
    }

    fun getWeeklySubscriptionWithTimeslot(orderId: Int): Map<WeeklyOnce, TimeSlot> {
        return getSubscriptionDataSource.getWeeklySubscriptionAndTimeSlot(orderId)
    }

    fun getDailySubscriptionWithTimeslot(orderId: Int): Map<DailySubscription, TimeSlot> {
        return getSubscriptionDataSource.getDailySubscriptionAndTimeSlot(orderId)
    }


    fun deleteFromWeeklySubscription(weeklyOnce: WeeklyOnce){
        updateSubscriptionDataSource.deleteFromWeeklySubscription(weeklyOnce)
    }

    fun deleteFromMonthlySubscription(monthlyOnce: MonthlyOnce){
        updateSubscriptionDataSource.deleteFromMonthlySubscription(monthlyOnce)
    }

    fun deleteFromDailySubscription(dailySubscription: DailySubscription){
        updateSubscriptionDataSource.deleteFromDailySubscription(dailySubscription)
    }
    fun addTimeSlot(timeSlot: TimeSlot) {
        addSubscriptionDataSource.addTimeSlot(timeSlot)
    }

    fun addMonthlyOnceSubscription(monthlyOnce: MonthlyOnce) {
        addSubscriptionDataSource.addMonthlyOnceSubscription(monthlyOnce)
    }

    fun addWeeklyOnceSubscription(weeklyOnce: WeeklyOnce) {
        addSubscriptionDataSource.addWeeklyOnceSubscription(weeklyOnce)
    }

    fun addDailySubscription(dailySubscription: DailySubscription) {
        addSubscriptionDataSource.addDailySubscription(dailySubscription)
    }
}