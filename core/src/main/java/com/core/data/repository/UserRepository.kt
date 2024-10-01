package com.core.data.repository

import com.core.data.datasource.userdatasource.UserDataSource
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.core.domain.recentlyvieweditems.RecentlyViewedItems
import com.core.domain.search.SearchHistory

class UserRepository (private var userDataSource: UserDataSource){

    fun addSearchQueryInDb(searchHistory: SearchHistory){
        userDataSource.addSearchQueryInDb(searchHistory)
    }

    fun getSearchHistory(userId: Int):List<SearchHistory>{
        return userDataSource.getSearchHistory(userId)
    }

    fun addTimeSlot(timeSlot: TimeSlot){
        userDataSource.addTimeSlot(timeSlot)
    }

    fun updateTimeSlot(timeSlot: TimeSlot){
        userDataSource.updateTimeSlot(timeSlot)
    }

    fun deleteFromWeeklySubscription(weeklyOnce: WeeklyOnce){
        userDataSource.deleteFromWeeklySubscription(weeklyOnce)
    }

    fun deleteFromMonthlySubscription(monthlyOnce: MonthlyOnce){
        userDataSource.deleteFromMonthlySubscription(monthlyOnce)
    }

    fun deleteFromDailySubscription(dailySubscription: DailySubscription){
        userDataSource.deleteFromDailySubscription(dailySubscription)
    }

    fun getDailySubscription():List<DailySubscription>{
        return userDataSource.getDailySubscription()
    }

    fun getOrderTimeSlot():List<TimeSlot>{
        return userDataSource.getOrderTimeSlot()
    }

    fun getWeeklySubscriptionList():List<WeeklyOnce>{
        return userDataSource.getWeeklySubscriptionList()
    }

    fun getMonthlySubscriptionList():List<MonthlyOnce>{
        return userDataSource.getMonthlySubscriptionList()
    }

    fun getOrderedDayForWeekSubscription(orderId:Int):WeeklyOnce{
        return userDataSource.getOrderedDayForWeekSubscription(orderId)
    }

    fun getOrderedDayForMonthlySubscription(orderId:Int):MonthlyOnce{
        return userDataSource.getOrderForMonthlySubscription(orderId)
    }

    fun addProductInRecentlyViewedItems(recentlyViewedItem: RecentlyViewedItems){
        userDataSource.addProductInRecentlyViewedItems(recentlyViewedItem)
    }

    fun getOrderForDailySubscription(orderId:Int):DailySubscription{
        return userDataSource.getOrderForDailySubscription(orderId)
    }

    fun getOrderedTimeSlot(orderId:Int):TimeSlot{
        return userDataSource.getOrderedTimeSlot(orderId)
    }
    fun getProductsForQuery(query:String):List<String>{
        return userDataSource.getProductForQuery(query)
    }

    fun getProductForQueryName(query: String):List<String>{
        return userDataSource.getProductForQueryName(query)
    }

}