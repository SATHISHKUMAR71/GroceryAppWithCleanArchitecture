package com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppRetailerVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.userviews.offer.OfferFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment.Companion.productListFilterCount
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.adapter.CustomerRequestAdapter.Companion.requestList
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestdetail.CustomerRequestDetailFragment
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.adapter.CustomerRequestAdapter

class CustomerRequestListFragment : Fragment() {

    companion object{
        var customerName:String = ""
        var requestedDate:String = ""
        var customerRequest:String = ""
        var customerPhone:String = ""
        var customerEmail:String = ""
    }

    private lateinit var customerViewModel: CustomerRequestViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        productListFilterCount = 0
        OfferFragment.offerFilterCount = 0
        FilterFragment.list = null
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
        val view = inflater.inflate(R.layout.fragment_customer_request, container, false)
        val customerReqRV = view.findViewById<RecyclerView>(R.id.customerRequestRecyclerView)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        customerViewModel = ViewModelProvider(this,
            GroceryAppRetailerVMFactory(userDao, retailerDao)
        )[CustomerRequestViewModel::class.java]
        customerViewModel.getCustomerRequest()
        customerViewModel.customerRequestList.observe(viewLifecycleOwner){
            if(customerReqRV.adapter==null) {
                val adapter = CustomerRequestAdapter(customerViewModel, this)
                customerReqRV.adapter = adapter
                customerReqRV.layoutManager = LinearLayoutManager(context)
            }
            requestList = it.toMutableList()
            if(requestList.isEmpty()){
                customerReqRV.visibility = View.GONE
                view.findViewById<ImageView>(R.id.noDataFoundImage).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.noRequestAvailableFromTheUser).visibility =View.VISIBLE
            }
            else{
                customerReqRV.visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.noDataFoundImage).visibility = View.GONE
                view.findViewById<TextView>(R.id.noRequestAvailableFromTheUser).visibility =View.GONE
            }
        }

        customerViewModel.selectedOrderLiveData.observe(viewLifecycleOwner){
            if(it!=null) {
                customerViewModel.getCorrespondingCart(it.cartId)
            }
        }
        customerViewModel.correspondingCartLiveData.observe(viewLifecycleOwner){
            if(it!=null) {
                val customerDetailFragment = CustomerRequestDetailFragment()
                customerDetailFragment.arguments = Bundle().apply {
                    customerViewModel.selectedOrderLiveData.value?.let {selectedOrder ->
                        this.putInt("orderId",selectedOrder.orderId)
                        this.putInt("cartId",selectedOrder.cartId)
                        this.putInt("addressId",selectedOrder.addressId)
                        this.putString("paymentMode",selectedOrder.paymentMode)
                        this.putString("deliveryFrequency",selectedOrder.deliveryFrequency)
                        this.putString("paymentStatus",selectedOrder.paymentStatus)
                        this.putString("deliveryStatus",selectedOrder.deliveryStatus)
                        this.putString("deliveryDate",selectedOrder.deliveryDate)
                        this.putString("orderedDate",selectedOrder.orderedDate)
                    }
                }
                OrderListFragment.correspondingCartList = it
//                OrderListFragment.selectedOrder = customerViewModel.selectedOrderLiveData.value
                FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                    customerDetailFragment,"Request Detail Fragment")
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        InitialFragment.hideSearchBar.value = true
        InitialFragment.hideBottomNav.value = false
    }

    override fun onStop() {
        super.onStop()
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        customerViewModel.selectedOrderLiveData.value = null
        customerViewModel.correspondingCartLiveData.value = null
        customerViewModel.correspondingCartLiveData.removeObservers(viewLifecycleOwner)
        customerViewModel.selectedOrderLiveData.removeObservers(viewLifecycleOwner)
        customerViewModel.customerRequestList.removeObservers(viewLifecycleOwner)
    }
}