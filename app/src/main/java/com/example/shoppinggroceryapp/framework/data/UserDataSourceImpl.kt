package com.example.shoppinggroceryapp.framework.data

import com.core.data.datasource.userdatasource.UserDataSource
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.core.domain.products.ParentCategory
import com.core.domain.recentlyvieweditems.RecentlyViewedItems
import com.core.domain.search.SearchHistory
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.entity.order.DailySubscriptionEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.MonthlyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.TimeSlotEntity
import com.example.shoppinggroceryapp.framework.db.entity.order.WeeklyOnceEntity
import com.example.shoppinggroceryapp.framework.db.entity.recentlyvieweditems.RecentlyViewedItemsEntity
import com.example.shoppinggroceryapp.framework.db.entity.search.SearchHistoryEntity

class UserDataSourceImpl(private var userDao: UserDao, private var retailerDao: RetailerDao):UserDataSource,ConvertionHelper() {
    override fun getProductForQuery(query: String): List<String> {
        return userDao.getProductForQuery(query)
    }

    override fun getProductForQueryName(query: String): List<String> {
        return userDao.getProductForQueryName(query)
    }

    override fun addProductInRecentlyViewedItems(recentlyViewedItems: RecentlyViewedItems) {
        retailerDao.addProductInRecentlyViewedItems(RecentlyViewedItemsEntity(recentlyViewedItems.recentlyViewedId,recentlyViewedItems.userId,recentlyViewedItems.productId))
    }

    override fun getParentCategoryList(): List<ParentCategory> {
        return  userDao.getParentCategoryList().map { ParentCategory(it.parentCategoryName,it.parentCategoryImage,it.parentCategoryDescription,it.isEssential) }
    }

    override fun getChildName(parent: String): List<String> {
        return userDao.getChildName(parent).map { it.categoryName }
    }

    override fun addSearchQueryInDb(searchHistory: SearchHistory) {
        userDao.addSearchQueryInDb(SearchHistoryEntity(searchHistory.searchText,searchHistory.userId))
    }

    override fun getSearchHistory(userId: Int): List<SearchHistory> {
        return userDao.getSearchHistory(userId).map { SearchHistory(it.searchText,it.userId) }
    }



    override fun updateTimeSlot(timeSlot: TimeSlot) {
        retailerDao.updateTimeSlot(TimeSlotEntity(timeSlot.orderId,timeSlot.timeId))
    }

    override fun deleteFromWeeklySubscription(weeklyOnce: WeeklyOnce) {
        retailerDao.deleteFromWeeklySubscription(WeeklyOnceEntity(weeklyOnce.orderId,weeklyOnce.weekId))
    }

    override fun deleteFromMonthlySubscription(monthlyOnce: MonthlyOnce) {
        retailerDao.deleteFromMonthlySubscription(MonthlyOnceEntity(monthlyOnce.orderId,monthlyOnce.dayOfMonth))
    }

    override fun deleteFromDailySubscription(dailySubscription: DailySubscription) {
        retailerDao.deleteFromDailySubscription(DailySubscriptionEntity(dailySubscription.orderId))
    }

    override fun getDailySubscription(): List<DailySubscription> {
        return retailerDao.getDailySubscription().map { DailySubscription(it.orderId) }
    }

    override fun getOrderTimeSlot(): List<TimeSlot> {
        return retailerDao.getOrderTimeSlot().map { TimeSlot(it.orderId,it.timeId) }
    }

    override fun getWeeklySubscriptionList(): List<WeeklyOnce> {
        return retailerDao.getWeeklySubscriptionList().map { WeeklyOnce(it.orderId,it.weekId) }
    }

    override fun getMonthlySubscriptionList(): List<MonthlyOnce> {
        return retailerDao.getMonthlySubscriptionList().map { MonthlyOnce(it.orderId,it.dayOfMonth) }
    }

    override fun getOrderedDayForWeekSubscription(orderId: Int): WeeklyOnce {
        return retailerDao.getOrderedDayForWeekSubscription(orderId).let {
            WeeklyOnce(it.orderId,it.weekId)
        }
    }

    override fun getOrderForDailySubscription(orderId: Int): DailySubscription {
        return retailerDao.getOrderForDailySubscription(orderId).let {
            DailySubscription(it.orderId)
        }
    }

    override fun getOrderForMonthlySubscription(orderId: Int): MonthlyOnce {
        return retailerDao.getOrderedDayForMonthlySubscription(orderId).let {
            MonthlyOnce(it.orderId,it.dayOfMonth)
        }
    }

    override fun getOrderedTimeSlot(orderId: Int): TimeSlot {
        return retailerDao.getOrderedTimeSlot(orderId).let {
            TimeSlot(it.orderId,it.timeId)
        }
    }

}