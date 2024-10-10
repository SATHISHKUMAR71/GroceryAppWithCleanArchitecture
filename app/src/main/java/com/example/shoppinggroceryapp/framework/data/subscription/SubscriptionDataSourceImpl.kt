package com.example.shoppinggroceryapp.framework.data.subscription

import com.core.data.datasource.subscriptiondatasource.AddSubscriptionDataSource
import com.core.data.datasource.subscriptiondatasource.GetSubscriptionDataSource
import com.core.data.datasource.subscriptiondatasource.UpdateSubscriptionDataSource
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.order.DailySubscriptionEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.MonthlyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.TimeSlotEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.WeeklyOnceEntity

class SubscriptionDataSourceImpl(private val userDao: UserDao):AddSubscriptionDataSource,GetSubscriptionDataSource,UpdateSubscriptionDataSource {
    override fun addTimeSlot(timeSlot: TimeSlot) {
        userDao.addTimeSlot(TimeSlotEntity(timeSlot.orderId,timeSlot.timeId))
    }

    override fun addMonthlyOnceSubscription(monthlyOnce: MonthlyOnce) {
        userDao.addMonthlyOnceSubscription(MonthlyOnceEntity(monthlyOnce.orderId,monthlyOnce.dayOfMonth))
    }

    override fun addWeeklyOnceSubscription(weeklyOnce: WeeklyOnce) {
        userDao.addWeeklyOnceSubscription(WeeklyOnceEntity(weeklyOnce.orderId,weeklyOnce.weekId))
    }

    override fun addDailySubscription(dailySubscription: DailySubscription) {
        userDao.addDailySubscription(DailySubscriptionEntity(dailySubscription.orderId))
    }

    override fun getOrderedDayForWeekSubscription(orderId: Int): WeeklyOnce? {
        return userDao.getOrderedDayForWeekSubscription(orderId)?.let {
            WeeklyOnce(it.orderId,it.weekId)
        }
    }

    override fun getOrderForDailySubscription(orderId: Int): DailySubscription? {
        return userDao.getOrderForDailySubscription(orderId)?.let {
            DailySubscription(it.orderId)
        }
    }

    override fun getOrderForMonthlySubscription(orderId: Int): MonthlyOnce? {
        return userDao.getOrderedDayForMonthlySubscription(orderId)?.let {
            MonthlyOnce(it.orderId,it.dayOfMonth)
        }
    }

    override fun getOrderedTimeSlot(orderId: Int): TimeSlot? {
        return userDao.getOrderedTimeSlot(orderId)?.let {
            TimeSlot(it.orderId,it.timeId)
        }
    }

    override fun getMonthlySubscriptionAndTimeSlot(orderId: Int): Map<MonthlyOnce, TimeSlot> {
        val convertedMap = mutableMapOf<MonthlyOnce,TimeSlot>()
        for(i in userDao.getMonthlyOnceWithTimeSlot(orderId)){
            convertedMap[MonthlyOnce(i.key.orderId,i.key.dayOfMonth)] = TimeSlot(i.value.orderId,i.value.timeId)
        }
        return convertedMap
    }

    override fun getWeeklySubscriptionAndTimeSlot(orderId: Int): Map<WeeklyOnce, TimeSlot> {
        val convertedMap = mutableMapOf<WeeklyOnce,TimeSlot>()
        for(i in userDao.getWeeklyOnceWithTimeSlot(orderId)){
            convertedMap[WeeklyOnce(i.key.orderId,i.key.weekId)] = TimeSlot(i.value.orderId,i.value.timeId)
        }
        return convertedMap
    }

    override fun getDailySubscriptionAndTimeSlot(orderId: Int): Map<DailySubscription, TimeSlot> {
        val convertedMap = mutableMapOf<DailySubscription,TimeSlot>()
        for(i in userDao.getDailySubscriptionWithTimeSlot(orderId)){
            convertedMap[DailySubscription(i.key.orderId)] = TimeSlot(i.value.orderId,i.value.timeId)
        }
        return convertedMap
    }

    override fun updateTimeSlot(timeSlot: TimeSlot) {
        userDao.updateTimeSlot(TimeSlotEntity(timeSlot.orderId,timeSlot.timeId))
    }

    override fun deleteFromWeeklySubscription(weeklyOnce: WeeklyOnce) {
        userDao.deleteFromWeeklySubscription(WeeklyOnceEntity(weeklyOnce.orderId,weeklyOnce.weekId))
    }

    override fun deleteFromMonthlySubscription(monthlyOnce: MonthlyOnce) {
        userDao.deleteFromMonthlySubscription(MonthlyOnceEntity(monthlyOnce.orderId,monthlyOnce.dayOfMonth))
    }

    override fun deleteFromDailySubscription(dailySubscription: DailySubscription) {
        userDao.deleteFromDailySubscription(DailySubscriptionEntity(dailySubscription.orderId))
    }

}