package com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
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
import com.example.shoppinggroceryapp.MainActivity
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
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.adapter.CustomerRequestAdapter.Companion.requestList
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestdetail.CustomerRequestDetailFragment
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.adapter.CustomerRequestAdapter
import com.example.shoppinggroceryapp.views.sharedviews.filter.ResetFilterValues
import com.google.android.material.appbar.MaterialToolbar

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
//        OfferFragment.offerFilterCount = 0
        FilterFragment.list = null
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
        val view = inflater.inflate(R.layout.fragment_customer_request, container, false)
        val customerReqRV = view.findViewById<RecyclerView>(R.id.customerRequestRecyclerView)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        customerViewModel = ViewModelProvider(this,
            GroceryAppRetailerVMFactory(userDao, retailerDao)
        )[CustomerRequestViewModel::class.java]
        val navToolBar = view.findViewById<MaterialToolbar>(R.id.materialToolbar)
        if(MainActivity.isRetailer) {
            customerViewModel.getCustomerRequest()
            navToolBar.navigationIcon = null
        }
        else{
            customerReqRV.setPadding(0)
            navToolBar.title = "Your Requests"
            navToolBar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            navToolBar.navigationIcon = ContextCompat.getDrawable(requireContext(),R.drawable.arrow_back_24px)
            customerViewModel.getSpecificCustomerReq(MainActivity.userId.toInt())
        }
        customerViewModel.customerRequestList.observe(viewLifecycleOwner){
            if(customerReqRV.adapter==null) {
                val adapter = CustomerRequestAdapter(customerViewModel, this)
                customerReqRV.adapter = adapter
                customerReqRV.layoutManager = LinearLayoutManager(context)
            }
            requestList = it.toMutableList()
            if(requestList.isEmpty()){
                customerReqRV.visibility = View.GONE
//                view.findViewById<ImageView>(R.id.noDataFoundImage).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.noRequestAvailableFromTheUser).visibility =View.VISIBLE
            }
            else{
                customerReqRV.visibility = View.VISIBLE
//                view.findViewById<ImageView>(R.id.noDataFoundImage).visibility = View.GONE
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
                    for(i in it.indices){
                        putLong("productId$i",it[i].productId)
                        putString("mainImage$i",it[i].mainImage)
                        putString("productName$i",it[i].productName)
                        putString("productDescription$i",it[i].productDescription)
                        putInt("totalItems$i",it[i].totalItems)
                        putFloat("unitPrice$i",it[i].unitPrice)
                        putString("manufactureDate$i",it[i].manufactureDate)
                        putString("expiryDate$i",it[i].expiryDate)
                        putString("productQuantity$i",it[i].productQuantity)
                        putString("brandName$i",it[i].brandName)
                    }
                }
//                OrderListFragment.correspondingCartList = it
//                OrderListFragment.selectedOrder = customerViewModel.selectedOrderLiveData.value
                FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                    customerDetailFragment,"Request Detail Fragment")
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        InitialFragment.hideBottomNav.value = !MainActivity.isRetailer
        InitialFragment.hideSearchBar.value = true

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