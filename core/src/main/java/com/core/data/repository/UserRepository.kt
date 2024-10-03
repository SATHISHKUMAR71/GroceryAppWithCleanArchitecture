package com.core.data.repository


import com.core.data.datasource.userdatasource.UserDataSource
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.core.domain.products.ParentCategory
import com.core.domain.recentlyvieweditems.RecentlyViewedItems
import com.core.domain.search.SearchHistory
import com.core.domain.user.User

class UserRepository (private val userDataSource: UserDataSource){


    fun addNewUser(user: User) {
        userDataSource.addNewUser(user)
    }

    fun updateExistingUser(user: User) {
        userDataSource.updateUser(user)
    }

}