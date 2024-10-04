package com.example.shoppinggroceryapp.views.userviews.cartview.cart

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.MutableLiveData
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
import com.core.domain.user.Address
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
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.userviews.category.CategoryFragment
import com.example.shoppinggroceryapp.views.userviews.addressview.getaddress.GetNewAddress
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary.OrderSummaryFragment
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressList
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.io.File

class CartFragment : Fragment() {

    companion object{
        var viewPriceDetailData = MutableLiveData(49f)
        var cartItemsSize = 0
        var selectedAddressEntity: Address? = null
    }
    var noOfItemsInt = 0
    private var continuePressed = 0

    private lateinit var noOfItems:TextView
    private lateinit var recyclerView:RecyclerView
    private lateinit var bottomLayout:LinearLayout
    private lateinit var price:MaterialButton
    private lateinit var adapter: ProductListAdapter
    private lateinit var cartViewModel: CartViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        noOfItemsInt = 0
        val view =  inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.cartList)
        var fileDir = File(requireContext().filesDir,"AppImages")
        val db = AppDatabase.getAppDatabase(requireContext()).getUserDao()
        val deliveryAddressNotFound = view.findViewById<LinearLayout>(R.id.deliveryAddressLayoutNotFound)
        val deliveryAddressFound = view.findViewById<LinearLayout>(R.id.deliveryAddressLayout)
        val addressOwnerName = view.findViewById<TextView>(R.id.addressOwnerName)
        val address = view.findViewById<TextView>(R.id.address)
        val addNewAddress = view.findViewById<MaterialButton>(R.id.addNewAddressButton)
        val changeAddress = view.findViewById<MaterialButton>(R.id.changeAddressButton)
        val addMoreGrocery = view.findViewById<MaterialButton>(R.id.addMoreGroceryButton)
        val addressContactNumber = view.findViewById<TextView>(R.id.addressPhone)
        bottomLayout = view.findViewById<LinearLayout>(R.id.linearLayout11)
        price = view.findViewById<MaterialButton>(R.id.viewPriceDetailsButton)
        val priceDetails = view.findViewById<LinearLayout>(R.id.linearLayout12)
        noOfItems = view.findViewById<TextView>(R.id.priceDetailsMrpTotalItems)
        val emptyCart = view.findViewById<ImageView>(R.id.emptyCartImage)
        val totalAmount =view.findViewById<TextView>(R.id.priceDetailsMrpPrice)
        val continueButton = view.findViewById<MaterialButton>(R.id.continueButton)
        val grandTotalAmount = view.findViewById<TextView>(R.id.priceDetailsTotalAmount)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()

        cartViewModel = ViewModelProvider(this,
            GroceryAppUserVMFactory(userDao, retailerDao)
        )[CartViewModel::class.java]
        addMoreGrocery.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, CategoryFragment(),"Added More Groceries")
        }

        adapter = ProductListAdapter(this,fileDir,"C",false,productListViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductListViewModel::class.java])
        adapter.setProducts(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        price.setOnClickListener {
            view.findViewById<NestedScrollView>(R.id.nestedScrollView).fullScroll(View.FOCUS_DOWN)
            view.findViewById<NestedScrollView>(R.id.nestedScrollView).fullScroll(View.FOCUS_DOWN)
        }

        viewPriceDetailData.observe(viewLifecycleOwner){
            if(it==49f){
                recyclerView.visibility = View.GONE
                priceDetails.visibility =View.GONE
                bottomLayout.visibility =View.GONE
                emptyCart.visibility = View.VISIBLE
            }
            else{
                recyclerView.visibility = View.VISIBLE
                priceDetails.visibility =View.VISIBLE
                bottomLayout.visibility =View.VISIBLE
                emptyCart.visibility = View.GONE
                noOfItemsInt = ProductListAdapter.productsSize
                val str = "MRP ($noOfItemsInt) Products"
                noOfItems.text =str
            }
            val str = "₹$it\nView Price Details"
            val grandTot = "₹$it"
            val totalAmt = "₹${it-49}"
            grandTotalAmount.text = grandTot

            totalAmount.text =totalAmt
            price.text = str
        }
        viewPriceDetailData.value = 49f
        cartViewModel.calculateInitialPrice(MainActivity.cartId)
        cartViewModel.totalPrice.observe(viewLifecycleOwner){
            viewPriceDetailData.value = it
        }
        cartViewModel.getAddressListForUser(MainActivity.userId.toInt())
        cartViewModel.addressEntityList.observe(viewLifecycleOwner){ addressList ->
            if (addressList.isEmpty()) {
                deliveryAddressNotFound.visibility = View.VISIBLE
                deliveryAddressFound.visibility = View.GONE
            } else {
                deliveryAddressFound.visibility = View.VISIBLE
                deliveryAddressNotFound.visibility = View.GONE
                if(selectedAddressEntity ==null){
                    selectedAddressEntity = addressList[0]
                }
                addressOwnerName.text = selectedAddressEntity?.addressContactName
                val addressVal = "${selectedAddressEntity?.buildingName}, ${selectedAddressEntity?.streetName}, ${selectedAddressEntity?.city}, ${selectedAddressEntity?.state}\n${selectedAddressEntity?.postalCode}"
                address.text =addressVal
                addressContactNumber.text = selectedAddressEntity?.addressContactNumber
            }
        }
        continueButton.setOnClickListener {
            if(selectedAddressEntity ==null){
                Snackbar.make(view,"Please Add the Delivery Address to order Items",Toast.LENGTH_SHORT).setBackgroundTint(Color.RED).show()
            }
            else{
                val orderSummaryFragment = OrderSummaryFragment()
                orderSummaryFragment.arguments = Bundle().apply {
                    putInt("noOfItems",noOfItemsInt)
                }
                FragmentTransaction.navigateWithBackstack(parentFragmentManager,orderSummaryFragment,"Order Summary Fragment")
            }
        }
        cartViewModel.getProductsByCartId(MainActivity.cartId)
        cartViewModel.cartProducts.observe(viewLifecycleOwner){
            adapter.setProducts(it)
            noOfItemsInt = it.size
            val str = "MRP ($noOfItemsInt) Products"
            price.visibility =View.VISIBLE
            noOfItems.text =str
        }
        addNewAddress.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, GetNewAddress(),"Add New Address")
        }

        changeAddress.setOnClickListener {
            val savedAddressListFragment = SavedAddressList()
            savedAddressListFragment.arguments = Bundle().apply {
                putBoolean("clickable",true)
            }
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,savedAddressListFragment,"Add New Address")
        }

        return view
    }


    override fun onStart() {
        super.onStart()
        InitialFragment.hideSearchBar.value = true
    }

    override fun onStop() {
        super.onStop()
        recyclerView.stopScroll()
    }
    override fun onPause() {
        super.onPause()
        InitialFragment.hideSearchBar.value = false
    }
}