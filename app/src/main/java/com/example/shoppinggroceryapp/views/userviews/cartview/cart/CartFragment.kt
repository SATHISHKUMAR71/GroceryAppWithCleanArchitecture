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
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.domain.user.Address
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.helpers.snackbar.ShowShortSnackBar
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.userviews.category.CategoryFragment
import com.example.shoppinggroceryapp.views.userviews.addressview.getaddress.GetNewAddress
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary.OrderSummaryFragment
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressList
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton

import java.io.File

class CartFragment : Fragment() {

    companion object{
        var viewPriceDetailData = MutableLiveData(49f)
        var cartItemsSize = 0
        var selectedAddressEntity: Address? = null
    }

    var noOfItemsInt = 0
    var savedPosition:Int? = null
    private lateinit var recyclerView:RecyclerView
    private lateinit var bottomLayout:LinearLayout
    private lateinit var price:MaterialButton
    private lateinit var adapter: ProductListAdapter
    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noOfItemsInt = 0
        val view =  inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.cartList)
        var fileDir = File(requireContext().filesDir,"AppImages")
        val db = AppDatabase.getAppDatabase(requireContext()).getUserDao()
        bottomLayout = view.findViewById<LinearLayout>(R.id.linearLayout11)
        price = view.findViewById<MaterialButton>(R.id.viewPriceDetailsButton)

        val continueButton = view.findViewById<MaterialButton>(R.id.continueButton)

        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()

        cartViewModel = ViewModelProvider(this, GroceryAppUserVMFactory(userDao, retailerDao))[CartViewModel::class.java]

        adapter = ProductListAdapter(this,fileDir,"C",false,productListViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductListViewModel::class.java])
//        adapter.setProducts(listOf())
        if(recyclerView.adapter == null){
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        price.setOnClickListener {
//            view.findViewById<AppBarLayout>(R.id.carttoolbar).setExpanded(false,false)
            (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(noOfItemsInt,-50)
        }
        cartViewModel.getProductsByCartId(MainActivity.cartId)
        cartViewModel.cartProducts.observe(viewLifecycleOwner){
            adapter.setProducts(it)
            noOfItemsInt = it.size
            val str = "MRP ($noOfItemsInt) Products"
            price.visibility =View.VISIBLE
            adapter.noOfItemLiveData.value = str
            savedPosition?.let {value ->
                println("POSITION rvrv $value")

                (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(value)
                if(value+2>noOfItemsInt){
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(value-1)
                }
            }
        }

        viewPriceDetailData.observe(viewLifecycleOwner){
            var mrpProductsText = ""
            if(it==49f){
                adapter.isVisible.value = false
                bottomLayout.visibility =View.GONE
            }
            else{
                adapter.isVisible.value = true
                bottomLayout.visibility =View.VISIBLE
                noOfItemsInt = ProductListAdapter.productsSize
                mrpProductsText = "MRP ($noOfItemsInt) Products"
            }
            val str = "₹$it\nView Price Details"
            val grandTot = "₹$it"
            val totalAmt = "₹${it-49}"

            price.text = str
            adapter.updatePriceDetails(totalAmt,grandTot,mrpProductsText)
        }
        viewPriceDetailData.value = 49f
        cartViewModel.calculateInitialPrice(MainActivity.cartId)
        cartViewModel.totalPrice.observe(viewLifecycleOwner){
            viewPriceDetailData.value = it
        }
        cartViewModel.getAddressListForUser(MainActivity.userId.toInt())

        continueButton.setOnClickListener {
            if(selectedAddressEntity ==null){
                ShowShortSnackBar.showRedColor(view,"Please Add the Delivery Address to order Items")
            }
            else{
                val orderSummaryFragment = OrderSummaryFragment()
                orderSummaryFragment.arguments = Bundle().apply {
                    putInt("noOfItems",noOfItemsInt)
                }
                FragmentTransaction.navigateWithBackstack(parentFragmentManager,orderSummaryFragment,"Order Summary Fragment")
            }
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
        savedPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }


    override fun onDestroy() {
        super.onDestroy()
        savedPosition = null
    }
}