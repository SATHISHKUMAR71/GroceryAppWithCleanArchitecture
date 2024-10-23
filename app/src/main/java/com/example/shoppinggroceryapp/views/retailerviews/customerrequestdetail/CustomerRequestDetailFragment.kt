package com.example.shoppinggroceryapp.views.retailerviews.customerrequestdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.CustomerRequestListFragment
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail.OrderDetailFragment

import com.google.android.material.appbar.MaterialToolbar

class CustomerRequestDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_request_detail, container, false)
        val orderDetailFrag = OrderDetailFragment()
        val selectedOrder = arguments?.let {
            OrderDetails(
                it.getInt("orderId",-1),
                it.getInt("cartId",-1) ,
                it.getInt("addressId",-1),
                it.getString("paymentMode",""),
                it.getString("deliveryFrequency",""),
                it.getString("paymentStatus",""),
                it.getString("deliveryStatus",""),
                it.getString("deliveryDate",""),
                it.getString("orderedDate",""),
            )
        }
        val cartProductsData = mutableListOf<CartWithProductData>()
        var i=0
        var productId:Long = -1
        var mainImageI:String? = null
        var productNameI:String? = null
        var descriptionI:String? =null
        var manufactureDateI:String? = null
        var expiryDateI:String? = null
        var totalItemsI = -1
        var unitPriceI = -1f
        var productQuantityI:String? = null
        var brandNameI:String? =null
        while (true){
            arguments?.let {
                productId = it.getLong("productId$i")
                mainImageI = it.getString("mainImage$i")
                productNameI = it.getString("productName$i")
                descriptionI = it.getString("productDescription$i")
                totalItemsI = it.getInt("totalItems$i")
                unitPriceI = it.getFloat("unitPrice$i")
                manufactureDateI = it.getString("manufactureDate$i")
                expiryDateI = it.getString("expiryDate$i")
                productQuantityI = it.getString("productQuantity$i")
                brandNameI = it.getString("brandName$i")
            }
            if(manufactureDateI==null && mainImageI==null && productQuantityI==null && productNameI==null && descriptionI==null && expiryDateI==null && brandNameI==null){
                break
            }
            cartProductsData.add(CartWithProductData(productId,mainImageI,productNameI?:"",descriptionI?:"",totalItemsI,unitPriceI,manufactureDateI?:"",expiryDateI?:"",productQuantityI?:"",brandNameI?:""))
            i++
        }
        orderDetailFrag.arguments = Bundle().apply {
            putBoolean("hideToolBar",true)
            putBoolean("hideButtons",true)
            selectedOrder?.let {selectedOrders ->
                this.putInt("orderId",selectedOrders.orderId)
                this.putInt("cartId",selectedOrders.cartId)
                this.putInt("addressId",selectedOrders.addressId)
                this.putString("paymentMode",selectedOrders.paymentMode)
                this.putString("deliveryFrequency",selectedOrders.deliveryFrequency)
                this.putString("paymentStatus",selectedOrders.paymentStatus)
                this.putString("deliveryStatus",selectedOrders.deliveryStatus)
                this.putString("deliveryDate",selectedOrders.deliveryDate)
                this.putString("orderedDate",selectedOrders.orderedDate)
            }
            for(i in cartProductsData.indices){
                putLong("productId$i",cartProductsData[i].productId)
                putString("mainImage$i", cartProductsData[i].mainImage)
                putString("productName$i", cartProductsData[i].productName)
                putString("productDescription$i", cartProductsData[i].productDescription)
                putInt("totalItems$i", cartProductsData[i].totalItems)
                putFloat("unitPrice$i", cartProductsData[i].unitPrice)
                putString("manufactureDate$i", cartProductsData[i].manufactureDate)
                putString("expiryDate$i", cartProductsData[i].expiryDate)
                putString("productQuantity$i", cartProductsData[i].productQuantity)
                putString("brandName$i", cartProductsData[i].brandName)
            }
        }
        view.findViewById<MaterialToolbar>(R.id.customerRequestToolbar).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        if(!MainActivity.isRetailer){
            view.findViewById<MaterialToolbar>(R.id.customerRequestToolbar).title = "Your Request"
        }
        view.findViewById<TextView>(R.id.customerName).text = CustomerRequestListFragment.customerName
        val email = "Email: ${CustomerRequestListFragment.customerEmail}"
        val phone = "Phone: ${CustomerRequestListFragment.customerPhone}"
        view.findViewById<TextView>(R.id.customerEmail).text = email
        view.findViewById<TextView>(R.id.customerPhone).text = phone
        view.findViewById<TextView>(R.id.customerRequestText).text = CustomerRequestListFragment.customerRequest
        val requestedOn ="Requested On: ${CustomerRequestListFragment.requestedDate}"
        view.findViewById<TextView>(R.id.requestedDate).text = requestedOn
        parentFragmentManager.beginTransaction()
            .replace(R.id.orderDetailsFragment,orderDetailFrag)
            .commit()
        return view
    }

    override fun onStop() {
        super.onStop()

        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }
}