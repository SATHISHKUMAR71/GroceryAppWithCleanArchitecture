package com.core.data.datasource.userdatasource

import com.core.domain.recentlyvieweditems.RecentlyViewedItems

interface UserDataSource:SearchDataSource,SubscriptionDataSource{
    fun getProductForQuery(query:String):List<String>
    fun getProductForQueryName(query:String):List<String>
    fun addProductInRecentlyViewedItems(recentlyViewedItems:RecentlyViewedItems)
}