package com.example.shoppinggroceryapp.views.userviews.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.data.repository.AddressRepository
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.products.ParentCategory
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.data.authentication.AuthenticationDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.address.AddressDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.cart.CartDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.help.HelpDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.order.OrderDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.product.ProductDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.search.SearchDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.subscription.SubscriptionDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.user.UserDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageLoaderAndGetter
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.sharedviews.filter.ResetFilterValues
import com.example.shoppinggroceryapp.views.userviews.category.adapter.MainCategoryAdapter
import com.example.shoppinggroceryapp.views.userviews.offer.OfferFragment


class CategoryFragment: Fragment() {

    private lateinit var mainCategoryRV:RecyclerView
    private lateinit var imageLoader: ImageLoaderAndGetter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLoader = ImageLoaderAndGetter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        OfferFragment.offerFilterCount = 0
        FilterFragment.badgeNumber = 0
        ResetFilterValues.resetFilterValues()
        OfferFragment.dis10Val = false
        OfferFragment.dis20Val = false
        OfferFragment.dis30Val = false
        OfferFragment.dis40Val = false
        OfferFragment.dis50Val =false
        ProductListFragment.dis10Val = false
        ProductListFragment.dis20Val = false
        ProductListFragment.dis30Val = false
        ProductListFragment.dis40Val = false
        ProductListFragment.dis50Val = false
        FilterFragment.list = null
        val view =  inflater.inflate(R.layout.fragment_category, container, false)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        val categoryViewModel = ViewModelProvider(this,
           GroceryAppUserVMFactory(userDao, retailerDao)
        )[CategoryViewModel::class.java]
        mainCategoryRV = view.findViewById(R.id.categoryRecyclerView)
        categoryViewModel.getParentAndChildNames()

        categoryViewModel.mappedList.observe(viewLifecycleOwner){
            val childList1 = categoryViewModel.getChildList(it)
            val parentList1 = categoryViewModel.getParentList(it)
            if(mainCategoryRV.adapter==null) {
                mainCategoryRV.adapter =
                    MainCategoryAdapter(this, parentList1, childList1, imageLoader)
                mainCategoryRV.layoutManager = LinearLayoutManager(requireContext())
            }
        }
        return view
    }


    override fun onStop() {
        super.onStop()
        mainCategoryRV.stopScroll()
    }
    override fun onDestroy() {
        super.onDestroy()
        MainCategoryAdapter.expandedData = mutableSetOf()
        mainCategoryRV.adapter = null
    }

}