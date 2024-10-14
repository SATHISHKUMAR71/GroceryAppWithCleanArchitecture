package com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.core.domain.order.Cart
import com.core.domain.products.Product
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.views.userviews.cartview.FindNumberOfCartItems
import com.example.shoppinggroceryapp.helpers.imagehandlers.SetProductImage
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.example.shoppinggroceryapp.views.userviews.cartview.diffutil.CartItemsDiffUtil
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.framework.db.entity.order.CartEntity
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail.ProductDetailFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.userviews.addressview.getaddress.GetNewAddress
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressList
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment.Companion.selectedAddressEntity
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartViewModel
import com.example.shoppinggroceryapp.views.userviews.category.CategoryFragment
import com.google.android.material.button.MaterialButton
import java.io.File

class ProductListAdapter(var fragment: Fragment,
                         private var file: File,
                         private var tag:String,private var isShort:Boolean,var productListViewModel: ProductListViewModel):RecyclerView.Adapter<ProductListAdapter.ProductLargeImageHolder>() {

    companion object{
        var productsSize = 0
    }
    var size = 0
    var grandTolAmt = ""
    var totAmtWithFee = ""
    var noOfItem = ""
    var isVisible:MutableLiveData<Boolean> = MutableLiveData()
    var grandTolAmtLiveData:MutableLiveData<String> = MutableLiveData()
    var totAmtWithFeeLiveData:MutableLiveData<String> = MutableLiveData()
    var noOfItemLiveData:MutableLiveData<String> = MutableLiveData()
    var productEntityList:MutableList<Product> = mutableListOf()

    private var countList = mutableListOf<Int>()
    init {
        for(i in 0..<productEntityList.size){
            countList.add(i,0)
        }
    }


    inner class ProductLargeImageHolder(productLargeView:View):RecyclerView.ViewHolder(productLargeView){

//        val productImage = productLargeView.findViewById<ImageView>(R.id.productImageLong)
//        val buttonLayout = productLargeView.findViewById<LinearLayout>(R.id.buttonLayout)
//        val brandName = productLargeView.findViewById<TextView>(R.id.brandName)
//        val productName = productLargeView.findViewById<TextView>(R.id.productNameLong)
//        val productQuantity = productLargeView.findViewById<TextView>(R.id.productQuantity)
//        val productExpiryDate = productLargeView.findViewById<TextView>(R.id.productExpiryDate)
//        val productPrice = productLargeView.findViewById<TextView>(R.id.productPriceLong)
//        val offer = productLargeView.findViewById<TextView>(R.id.offerText)
//        val productAddRemoveLayout:LinearLayout = productLargeView.findViewById(R.id.productAddRemoveLayout)
//        val productAddOneTime:MaterialButton = productLargeView.findViewById(R.id.productAddLayoutOneTime)
//        val totalItems:TextView = productLargeView.findViewById(R.id.totalItemsAdded)
//        val addSymbolButton:ImageButton = productLargeView.findViewById(R.id.productAddSymbolButton)
//        val removeSymbolButton:ImageButton = productLargeView.findViewById(R.id.productRemoveSymbolButton)
//        val productMrpText:TextView = productLargeView.findViewById(R.id.productMrpText)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductLargeImageHolder {
        if(viewType!=-1 && viewType!=-2) {
            if (isShort) {

                return ProductLargeImageHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.product_layout_short, parent, false)
                )
            } else {
                return ProductLargeImageHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.product_layout_long, parent, false)
                )
            }
        }
        else if(viewType==-2){
            return ProductLargeImageHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.cart_address_view, parent, false))
        }
        else{
            return ProductLargeImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_price_detail_view,parent,false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(tag=="C" && position==productEntityList.size+1){
            -1
        }
        else if(tag=="C" && position==0){
            -2
        }
        else {
            super.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int {
        size = productEntityList.size
        return if(tag=="C"){
            size+2
        }
        else {
            size
        }
    }

    override fun onBindViewHolder(holder: ProductLargeImageHolder, position: Int) {
        var position = position
        if((size==0) && (tag!="C")){

        }
        else{
            if(tag == "C" && position==0){
                val deliveryAddressNotFound = holder.itemView.findViewById<LinearLayout>(R.id.deliveryAddressLayoutNotFound)
                val deliveryAddressFound = holder.itemView.findViewById<LinearLayout>(R.id.deliveryAddressLayout)
                val addressOwnerName = holder.itemView.findViewById<TextView>(R.id.addressOwnerName)
                val address = holder.itemView.findViewById<TextView>(R.id.address)
                val addNewAddress = holder.itemView.findViewById<MaterialButton>(R.id.addNewAddressButton)
                val changeAddress = holder.itemView.findViewById<MaterialButton>(R.id.changeAddressButton)
                val addMoreGrocery = holder.itemView.findViewById<MaterialButton>(R.id.addMoreGroceryButton)
                val addressContactNumber = holder.itemView.findViewById<TextView>(R.id.addressPhone)
                val db1 = AppDatabase.getAppDatabase(fragment.requireContext())
                val userDao = db1.getUserDao()
                val retailerDao = db1.getRetailerDao()
                val cartViewModel = ViewModelProvider(fragment, GroceryAppUserVMFactory(userDao, retailerDao))[CartViewModel::class.java]
                cartViewModel.getAddressListForUser(MainActivity.userId.toInt())
                cartViewModel.addressEntityList.observe(fragment.viewLifecycleOwner){ addressList ->
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
                addNewAddress.setOnClickListener {
                    FragmentTransaction.navigateWithBackstack(fragment.parentFragmentManager, GetNewAddress(),"Add New Address")
                }

                changeAddress.setOnClickListener {
                    val savedAddressListFragment = SavedAddressList()
                    savedAddressListFragment.arguments = Bundle().apply {
                        putBoolean("clickable",true)
                    }
                    FragmentTransaction.navigateWithBackstack(fragment.parentFragmentManager,savedAddressListFragment,"Add New Address")
                }
                addMoreGrocery.setOnClickListener {
                    FragmentTransaction.navigateWithBackstack(fragment.parentFragmentManager, CategoryFragment(),"Added More Groceries")
                }
            }
            else if(tag=="C" && position==productEntityList.size+1){
                val grandTotalAmountMrp = holder.itemView.findViewById<TextView>(R.id.priceDetailsMrpPrice)
                val totalAmtWithDeliveryFee = holder.itemView.findViewById<TextView>(R.id.priceDetailsTotalAmount)
                val noOfItems = holder.itemView.findViewById<TextView>(R.id.priceDetailsMrpTotalItems)
                holder.itemView.findViewById<LinearLayout>(R.id.cartPriceDetailsLayout).visibility = View.GONE
                isVisible.observe(fragment.viewLifecycleOwner){
                    if(it) {
                        holder.itemView.findViewById<LinearLayout>(R.id.cartPriceDetailsLayout).visibility =
                            View.VISIBLE
                        holder.itemView.findViewById<ImageView>(R.id.emptyCartImage).visibility = View.GONE
                    }
                    else{
                        holder.itemView.findViewById<LinearLayout>(R.id.cartPriceDetailsLayout).visibility =
                            View.GONE
                        holder.itemView.findViewById<ImageView>(R.id.emptyCartImage).visibility = View.VISIBLE
                    }
                }
                noOfItemLiveData.observe(fragment.viewLifecycleOwner){
                    noOfItems.text = it
                }
                totAmtWithFeeLiveData.observe(fragment.viewLifecycleOwner){
                    totalAmtWithDeliveryFee.text = it
                }
                grandTolAmtLiveData.observe(fragment.viewLifecycleOwner){
                    grandTotalAmountMrp.text = it
                }

            }
            else {
                if(tag=="C"){
                    position-=1
                }
                if (MainActivity.isRetailer) {
                    holder.itemView.findViewById<LinearLayout>(R.id.buttonLayout).visibility = View.GONE
                } else {
                    holder.itemView.findViewById<LinearLayout>(R.id.buttonLayout).visibility = View.VISIBLE
                }
                productListViewModel.getSpecificCart(
                    MainActivity.cartId,
                    productEntityList[position].productId.toInt()
                ) { cart ->
                    if (cart != null) {
                        MainActivity.handler.post {
                            holder.itemView.findViewById<MaterialButton>(R.id.productAddLayoutOneTime).visibility = View.GONE
                            holder.itemView.findViewById<LinearLayout>(R.id.productAddRemoveLayout).visibility = View.VISIBLE
                            countList[position] = cart.totalItems
                            holder.itemView.findViewById<TextView>(R.id.totalItemsAdded).text = cart.totalItems.toString()
                        }
                    } else {
                        MainActivity.handler.post {
                            holder.itemView.findViewById<MaterialButton>(R.id.productAddLayoutOneTime).visibility = View.VISIBLE
                            holder.itemView.findViewById<LinearLayout>(R.id.productAddRemoveLayout).visibility = View.GONE
                            countList[position] = 0
                            holder.itemView.findViewById<TextView>(R.id.totalItemsAdded).text = "0"
                        }
                    }
                }

                productListViewModel.getBrandName(productEntityList[position].brandId) { brand ->
                    MainActivity.handler.post {
                        holder.itemView.findViewById<TextView>(R.id.brandName).text = brand
                    }
                }
                if (productEntityList[position].offer > 0f) {
                    val str = "MRP ₹" + productEntityList[position].price
                    holder.itemView.findViewById<TextView>(R.id.productMrpText).apply {
                        text = str
                        paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        visibility = View.VISIBLE
                    }
//                    holder.itemView.findViewById<TextView>(R.id.productMrpText).text = str
//                    holder.itemView.findViewById<TextView>(R.id.productMrpText).paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
//                    holder.itemView.findViewById<TextView>(R.id.productMrpText).visibility = View.VISIBLE

                    val offerText = productEntityList[position].offer.toInt().toString() + "% Off"
                    holder.itemView.findViewById<TextView>(R.id.offerText).apply {
                        visibility = View.VISIBLE
                        text = offerText
                    }
//                    holder.itemView.findViewById<TextView>(R.id.offerText).visibility = View.VISIBLE
//                    holder.itemView.findViewById<TextView>(R.id.offerText).text = offerText
                } else {
                    val str = "MRP"
                    holder.itemView.findViewById<TextView>(R.id.productMrpText).apply {
                        text = str
                        paintFlags = 0
                    }
//                    holder.itemView.findViewById<TextView>(R.id.productMrpText).text = str
//                    holder.itemView.findViewById<TextView>(R.id.productMrpText).paintFlags = 0
                    holder.itemView.findViewById<TextView>(R.id.offerText).apply {
                        text = null
                        visibility = View.GONE
                    }
//                    holder.itemView.findViewById<TextView>(R.id.offerText).text = null
//                    holder.itemView.findViewById<TextView>(R.id.offerText).visibility = View.GONE
                }
                holder.itemView.findViewById<TextView>(R.id.productNameLong).text = productEntityList[position].productName
                holder.itemView.findViewById<TextView>(R.id.productExpiryDate).text =
                    DateGenerator.getDayAndMonth(productEntityList[position].expiryDate)
                holder.itemView.findViewById<TextView>(R.id.productQuantity).text = productEntityList[position].productQuantity
                val price = "₹" + calculateDiscountPrice(
                    productEntityList[position].price,
                    productEntityList[position].offer
                )
                holder.itemView.findViewById<TextView>(R.id.productPriceLong).text = price
                val url = (productEntityList[position].mainImage)
                SetProductImage.setImageView(holder.itemView.findViewById(R.id.productImageLong), url, file)
                setUpListeners(holder, position)
            }
        }
    }

    private fun setUpListeners(holder: ProductLargeImageHolder, position: Int) {
        holder.itemView.setOnClickListener {
            try {
                ProductListFragment.selectedPos = position
                ProductListFragment.selectedProductEntity.value =
                    productEntityList[position]
                fragment.parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    .replace(R.id.fragmentMainLayout, ProductDetailFragment())
                    .addToBackStack("Product Detail Fragment")
                    .commit()
            }
            catch (e:Exception){

            }
        }

        holder.itemView.findViewById<ImageButton>(R.id.productRemoveSymbolButton).setOnClickListener {
            if((holder.absoluteAdapterPosition==position) || ((tag=="C") && (holder.absoluteAdapterPosition==position+1))) {
                val count = --countList[position]
                val positionVal = calculateDiscountPrice(productEntityList[position].price, productEntityList[position].offer)
                if (count == 0) {
                    productsSize--
                    if (tag == "P" || tag == "O") {
                        productListViewModel.getSpecificCart(MainActivity.cartId,productEntityList[position].productId.toInt()){ cart ->
                            if (cart != null) {
                                productListViewModel.removeProductInCart(cart)
                            }
                            ProductListFragment.totalCost.postValue(ProductListFragment.totalCost.value!! - positionVal)
                            CartFragment.viewPriceDetailData.postValue(CartFragment.viewPriceDetailData.value!! - positionVal)
                        }
                        FindNumberOfCartItems.productCount.value = FindNumberOfCartItems.productCount.value!!-1
                        holder.itemView.findViewById<LinearLayout>(R.id.productAddRemoveLayout).visibility = View.GONE
                        holder.itemView.findViewById<MaterialButton>(R.id.productAddLayoutOneTime).visibility = View.VISIBLE
                    } else if (tag == "C") {
                        productListViewModel.getSpecificCart(MainActivity.cartId,productEntityList[position].productId.toInt()){ cart ->
                            if (cart != null) {
                                productListViewModel.removeProductInCart(cart)
                            }
                            CartFragment.viewPriceDetailData.postValue(CartFragment.viewPriceDetailData.value!! - positionVal)
                        }
                        productEntityList.removeAt(position)
                        countList.removeAt(position)
                        notifyItemRemoved(position+1)
                        if(productEntityList.size==0){
                            notifyDataSetChanged()
                        }
                        else {
                            notifyItemRangeChanged(position + 1, productEntityList.size + 1)
                        }
                        FindNumberOfCartItems.productCount.value = FindNumberOfCartItems.productCount.value!!-1
                    }
                    holder.itemView.findViewById<TextView>(R.id.totalItemsAdded).text = "0"

                }
                else {
                    productListViewModel.updateItemsInCart(
                        if(productEntityList[position].offer==-1f) {
                            Cart(
                                MainActivity.cartId,
                                productEntityList[position].productId.toInt(),
                                count,
                                productEntityList[position].price
                            )
                        }
                        else
                        {
                            Cart(
                                MainActivity.cartId,
                                productEntityList[position].productId.toInt(),
                                count,calculateDiscountPrice(
                                    productEntityList[position].price,
                                    productEntityList[position].offer)
                            )
                        })
                    holder.itemView.findViewById<TextView>(R.id.totalItemsAdded).text = count.toString()
                    ProductListFragment.totalCost.value =
                        ProductListFragment.totalCost.value!! - positionVal
                    CartFragment.viewPriceDetailData.value =
                        CartFragment.viewPriceDetailData.value!! - positionVal
                }
            }
        }

        holder.itemView.findViewById<ImageButton>(R.id.productAddSymbolButton).setOnClickListener {
            if((holder.absoluteAdapterPosition==position) || ((tag=="C") && (holder.absoluteAdapterPosition==position+1))) {
                val count = ++countList[position]
                ProductListFragment.totalCost.value =
                    ProductListFragment.totalCost.value!! + calculateDiscountPrice(productEntityList[position].price, productEntityList[position].offer)
                CartFragment.viewPriceDetailData.value =
                    CartFragment.viewPriceDetailData.value!! + calculateDiscountPrice(productEntityList[position].price, productEntityList[position].offer)
                val cartEntity = if(productEntityList[position].offer==-1f) {
                    Cart(
                        MainActivity.cartId,
                        productEntityList[position].productId.toInt(),
                        count,
                        productEntityList[position].price
                    )
                }
                else
                { Cart(
                    MainActivity.cartId,
                    productEntityList[position].productId.toInt(),
                    count,calculateDiscountPrice(
                        productEntityList[position].price,
                        productEntityList[position].offer)
                )
                }
                productListViewModel.updateItemsInCart(cartEntity)
                holder.itemView.findViewById<TextView>(R.id.totalItemsAdded).text = count.toString()
            }
        }

        holder.itemView.findViewById<MaterialButton>(R.id.productAddLayoutOneTime).setOnClickListener {
            if((holder.absoluteAdapterPosition==position) || ((tag=="C") && (holder.absoluteAdapterPosition==position+1))) {
                val count = ++countList[position]
                productsSize++
                holder.itemView.findViewById<TextView>(R.id.totalItemsAdded).text = count.toString()
                ProductListFragment.totalCost.value =
                    ProductListFragment.totalCost.value!! + calculateDiscountPrice(productEntityList[position].price, productEntityList[position].offer)
                CartFragment.viewPriceDetailData.value =
                    CartFragment.viewPriceDetailData.value!! + calculateDiscountPrice(
                        productEntityList[position].price, productEntityList[position].offer)
                var cart = if(productEntityList[position].offer==-1f) {
                    Cart(
                        MainActivity.cartId,
                        productEntityList[position].productId.toInt(),
                        count,
                        productEntityList[position].price
                    )
                }
                else
                { Cart(
                    MainActivity.cartId,
                    productEntityList[position].productId.toInt(),
                    count,calculateDiscountPrice(
                        productEntityList[position].price,
                        productEntityList[position].offer)
                )
                }
                productListViewModel.updateItemsInCart(cart)
                FindNumberOfCartItems.productCount.value = FindNumberOfCartItems.productCount.value!!+1
                holder.itemView.findViewById<LinearLayout>(R.id.productAddRemoveLayout).visibility = View.VISIBLE
                holder.itemView.findViewById<MaterialButton>(R.id.productAddLayoutOneTime).visibility = View.GONE
            }
        }
    }

    fun setProducts(newList:List<Product>){

        if(tag=="C"){
            productsSize = newList.size
            for(i in newList.indices){
                countList.add(i,0)
            }
            productEntityList.clear()
            productEntityList.addAll(newList)
            notifyDataSetChanged()
        }
        else{
            productsSize = newList.size
            val diffUtil = CartItemsDiffUtil(productEntityList,newList)
            for(i in newList.indices){
                countList.add(i,0)
            }
            val diffResults = DiffUtil.calculateDiff(diffUtil)
            productEntityList.clear()
            productEntityList.addAll(newList)
            diffResults.dispatchUpdatesTo(this)
        }
    }




    private fun calculateDiscountPrice(price:Float, offer:Float):Float{
        return if(offer>0f) {
            price - (price * (offer / 100))
        } else{
            price
        }
    }

    fun updatePriceDetails(grandTotal:String,totalAmt:String,noOfItems:String){
        grandTolAmtLiveData.value = grandTotal
        totAmtWithFeeLiveData.value = totalAmt
        noOfItemLiveData.value = noOfItems
    }


}