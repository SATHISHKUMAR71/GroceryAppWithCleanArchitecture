package com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.core.data.repository.AddressRepository
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.Cart
import com.core.domain.products.Product
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
import com.example.shoppinggroceryapp.helpers.NotificationBuilder
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.views.userviews.cartview.FindNumberOfCartItems
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageLoaderAndGetter
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.example.shoppinggroceryapp.views.userviews.category.CategoryFragment
import com.example.shoppinggroceryapp.views.retailerviews.addeditproduct.AddOrEditProductFragment
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductImageAdapter
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.sharedviews.search.adapter.SearchListAdapter.Companion.searchList
import com.google.android.material.R.attr.textAppearanceBodyLarge
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.coroutineContext


class ProductDetailFragment : Fragment() {


    private var countOfOneProduct = 0
    private lateinit var imageLoader: ImageLoaderAndGetter
//    private lateinit var cartViewModel: CartViewModel
    private lateinit var recyclerView:RecyclerView
    private lateinit var productDetailToolBar:MaterialToolbar
    private lateinit var mrpTextView:TextView
    private lateinit var discountedPrice:TextView
    private lateinit var badgeDrawable:BadgeDrawable
    private lateinit var addProductButton:MaterialButton
    private lateinit var totalItemsAddedProductDetail:TextView
    private lateinit var removeProductImgButton:ImageButton
    private lateinit var addProductImgButton:ImageButton
    private lateinit var addRemoveLayout:LinearLayout
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var notificationBuilder: NotificationBuilder
    private var modifiedProductName:Product? = null
    var once = 0
    var oneTimeFragmentIn = -1
    var backNavigated = false
    var selectedProductEntityInClass: Product? = null
    var productObserved = 0
    companion object{
        var brandData:MutableLiveData<String> = MutableLiveData()
        var selectedProductEntityList = mutableListOf<Product>()
        var deletePosition:Int? = null
//        var productCount:MutableLiveData<Int> = MutableLiveData(0)
    }
    private lateinit var productDetailViewModel: ProductDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLoader = ImageLoaderAndGetter()
        oneTimeFragmentIn = 0

    }

    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_product_detail, container, false)
        val viewPager = view.findViewById<ViewPager2>(R.id.productImageViewer)
        productDetailToolBar = view.findViewById(R.id.productDetailToolbar)
        mrpTextView = view.findViewById(R.id.productPriceProductDetail)
        discountedPrice = view.findViewById(R.id.discountedPrice)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        productDetailViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductDetailViewModel::class.java]
        var productListViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductListViewModel::class.java]
        productDetailViewModel.getImagesForProducts(ProductListFragment.selectedProductEntity.value?.productId?:0)
        addProductButton = view.findViewById(R.id.addProductButtonProductDetail)
        totalItemsAddedProductDetail = view.findViewById(R.id.totalItemsAddedProductDetail)
        removeProductImgButton = view.findViewById(R.id.productRemoveSymbolButtonProductDetail)
        addProductImgButton = view.findViewById(R.id.productAddSymbolButtonProductDetail)
        addRemoveLayout = view.findViewById(R.id.productAddRemoveLayoutProductDetail)
        recyclerView = view.findViewById(R.id.productListInProductDetailFragment)

        if(MainActivity.isRetailer){
            productDetailToolBar.menu.findItem(R.id.edit).setVisible(true)
            productDetailToolBar.menu.findItem(R.id.delete).setVisible(true)
            productDetailToolBar.menu.findItem(R.id.cart).setVisible(false)
            productDetailToolBar.menu.findItem(R.id.addedInWishlist).setVisible(false)
            productDetailToolBar.menu.findItem(R.id.addToWishlist).setVisible(false)
//            recyclerView.setPadding(0,0,0,250)
            recyclerView.setPadding(recyclerView.paddingLeft,recyclerView.paddingTop,recyclerView.paddingRight,250)
            view.findViewById<NestedScrollView>(R.id.productDetailScrollView).setPadding(0)
            view.findViewById<CardView>(R.id.exploreBottomLayoutCard).visibility = View.GONE
        }

        else{
//            recyclerView.setPadding(10)
            productDetailToolBar.menu.findItem(R.id.edit).setVisible(false)
            productDetailToolBar.menu.findItem(R.id.cart).setVisible(true)
            productDetailToolBar.menu.findItem(R.id.delete).setVisible(false)
            productDetailToolBar.menu.findItem(R.id.addedInWishlist).setVisible(true)
            productDetailToolBar.menu.findItem(R.id.addToWishlist).setVisible(true)
            view.findViewById<CardView>(R.id.exploreBottomLayoutCard).visibility = View.VISIBLE
        }


        productDetailToolBar.setOnMenuItemClickListener { it ->
            when (it.itemId) {
                R.id.cart -> {
                    if (!MainActivity.isRetailer) {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            CartFragment(),"Cart Fragment")
                    }
                }
                R.id.edit -> {
                    if (MainActivity.isRetailer) {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            AddOrEditProductFragment(),"Add Or Edit Fragment")
                    }
                }
                R.id.addedInWishlist -> {
                    ShowShortToast.show("Removed From Wishlist",requireContext())
                    ProductListFragment.selectedProductEntity.value?.let {prod ->
                        productDetailViewModel.removeProductFromWishList(prod.productId,MainActivity.userId.toInt())
                    }
                    productDetailToolBar.menu.findItem(R.id.addedInWishlist).setVisible(false)
                    productDetailToolBar.menu.findItem(R.id.addToWishlist).setVisible(true)
                    resetBadge(badgeDrawable,productDetailToolBar)
                }
                R.id.addToWishlist -> {
                    ShowShortToast.show("Added To Wishlist",requireContext())
                    ProductListFragment.selectedProductEntity.value?.let {prod ->
                        productDetailViewModel.addProductToWishList(prod.productId,MainActivity.userId.toInt())
                    }
                    productDetailToolBar.menu.findItem(R.id.addedInWishlist).setVisible(true)
                    productDetailToolBar.menu.findItem(R.id.addToWishlist).setVisible(false)
                    resetBadge(badgeDrawable,productDetailToolBar)
                }
                R.id.delete -> {
                    if(MainActivity.isRetailer){
                        AlertDialog.Builder(context)
                            .setTitle("Delete Product!")
                            .setMessage("Are you Sure to delete this product in Inventory?")
                            .setNegativeButton("No"){dialog,which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("Yes"){dialog,which ->
                                ProductListFragment.selectedProductEntity.value?.let {
                                    productDetailViewModel.removeProduct(it)
                                }
                                deletePosition = ProductListFragment.selectedPos
                                dialog.dismiss()
                                notificationBuilder = NotificationBuilder(requireContext())
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    notificationBuilder.createNotificationChannel()
                                }
                                ProductListFragment.selectedProductEntity.value?.let {
                                    modifiedProductName = it
                                }
                                setProductValue()
                                modifiedProductName?.let {
                                    productDetailViewModel.getOrdersForThisProduct(it.productId)
                                }
                                ShowShortToast.show("Product Deleted Successfully",requireContext())
                                parentFragmentManager.popBackStack()
                            }
                            .create()
                            .show()
                    }
                }
            }
            true
        }
        productDetailViewModel.deletedProductUserInfo.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                productDetailViewModel.updateOrders(it)
                if(modifiedProductName!=null) {
                    notificationBuilder.showNotification(
                        modifiedProductName!!,
                        "${modifiedProductName?.productName} is Not Available in Order ${it[0].orderId}",
                        "The Ordered Products are removed from inventory",
                        "Some items in your order are out of stock. You can either cancel your order or keep it as is. We’ll deliver the available items. If all items are out of stock, your order will be canceled automatically."
                    )
                }
            }
        }
        badgeDrawable = BadgeDrawable.create(requireContext())



        productDetailViewModel.getProductsByCartId(MainActivity.cartId)
        productDetailViewModel.cartProducts.observe(viewLifecycleOwner){
            FindNumberOfCartItems.productCount.value = it.size
            var noOfItemsInt = FindNumberOfCartItems.productCount.value
            if(noOfItemsInt==0){
                badgeDrawable.isVisible = false
            }
            else{
                badgeDrawable.isVisible = true
                badgeDrawable.text = noOfItemsInt.toString()
            }
            BadgeUtils.attachBadgeDrawable(badgeDrawable,productDetailToolBar,R.id.cart)
        }
        ProductListFragment.selectedProductEntity.value?.brandId?.let{
            productDetailViewModel.getBrandName(it)
        }



        productDetailToolBar.setNavigationOnClickListener {
            setProductValue()
            parentFragmentManager.popBackStack()
        }

        ProductListFragment.selectedProductEntity.value?.let {
            productDetailViewModel.addInRecentlyViewedItems(it.productId)
        }

        productDetailViewModel.similarProductsLiveData.observe(viewLifecycleOwner){
            if(it.size == 1) {
                view.findViewById<LinearLayout>(R.id.similarProductsLayout).visibility = View.GONE
            }
            else {
                view.findViewById<LinearLayout>(R.id.similarProductsLayout).visibility = View.VISIBLE
                val adapter = ProductListAdapter(
                    this,
                    File(requireContext().filesDir, "AppImages"),
                    "P",
                    true,productListViewModel,null
                )
                recyclerView.adapter = adapter
                val tmpList = mutableListOf<Product>()
                for (i in it.toMutableList()) {
                    println("09890 product name and id: ${i.productId}  and ${i.productName}")
                    if (i.productId == ProductListFragment.selectedProductEntity.value?.productId) {
                        continue
                    }
                    tmpList.add(i)
                }
                if(tmpList.size<8){
                    adapter.setProducts(tmpList.subList(0,tmpList.size))
                }
                else{
                    adapter.setProducts(tmpList.subList(0,8))
                }
                recyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }
        ProductListFragment.selectedProductEntity.value?.let {
            if(it.availableItems==0){
                view.findViewById<TextView>(R.id.outOfStockInProductDetail).visibility = View.VISIBLE
                addProductButton.visibility = View.GONE
            }
            else if(it.availableItems<=countOfOneProduct){
                view.findViewById<TextView>(R.id.outOfStockInProductDetail).visibility = View.VISIBLE
            }
        }
        addProductButton.setOnClickListener {
            countOfOneProduct++
            ProductListFragment.selectedProductEntity.value?.let {
                if(it.availableItems<=countOfOneProduct){
                    view.findViewById<TextView>(R.id.outOfStockInProductDetail).visibility = View.VISIBLE
                    addProductButton.visibility = View.GONE
                }
            }
            productDetailViewModel.addProductInCart(Cart(MainActivity.cartId, ProductListFragment.selectedProductEntity.value!!.productId.toInt(), countOfOneProduct, productDetailViewModel.calculateDiscountPrice(ProductListFragment.selectedProductEntity.value!!.price, ProductListFragment.selectedProductEntity.value!!.offer)))
            totalItemsAddedProductDetail.text = countOfOneProduct.toString()
            addProductButton.visibility = View.GONE
            FindNumberOfCartItems.productCount.value =
                FindNumberOfCartItems.productCount.value!! + 1
            resetBadge(badgeDrawable, productDetailToolBar)
            addRemoveLayout.visibility = View.VISIBLE
        }
        productDetailViewModel.imageList.observe(viewLifecycleOwner){
            var imageList:MutableList<String> = mutableListOf()
            imageList.add(ProductListFragment.selectedProductEntity.value?.mainImage?:"")
            for(i in it){
                imageList.add(i.images)
            }
            viewPager.adapter  = ProductImageAdapter(this,imageList,imageLoader)
            TabLayoutMediator(view.findViewById(R.id.imageTabLayout),viewPager){tab,pos ->

            }.attach()
        }
        addProductImgButton.setOnClickListener {
            countOfOneProduct++
            ProductListFragment.selectedProductEntity.value?.let {
                if(it.availableItems<=countOfOneProduct){
                    view.findViewById<TextView>(R.id.outOfStockInProductDetail).visibility = View.VISIBLE
                    addProductImgButton.visibility = View.INVISIBLE
                }
            }
            productDetailViewModel.updateProductInCart(Cart(MainActivity.cartId, ProductListFragment.selectedProductEntity.value!!.productId.toInt(), countOfOneProduct, productDetailViewModel.calculateDiscountPrice(ProductListFragment.selectedProductEntity.value!!.price, ProductListFragment.selectedProductEntity.value!!.offer)))
            totalItemsAddedProductDetail.text = countOfOneProduct.toString()
        }
        removeProductImgButton.setOnClickListener {
            if (countOfOneProduct > 1) {
                countOfOneProduct--
                totalItemsAddedProductDetail.text = countOfOneProduct.toString()
                productDetailViewModel.updateProductInCart(
                    Cart(MainActivity.cartId, ProductListFragment.selectedProductEntity.value!!.productId.toInt(), countOfOneProduct, productDetailViewModel.calculateDiscountPrice(ProductListFragment.selectedProductEntity.value!!.price, ProductListFragment.selectedProductEntity.value!!.offer))
                )
            } else if (countOfOneProduct == 1) {
                countOfOneProduct--
                productDetailViewModel.removeProductInCart(Cart(MainActivity.cartId, ProductListFragment.selectedProductEntity.value!!.productId.toInt(), countOfOneProduct, productDetailViewModel.calculateDiscountPrice(ProductListFragment.selectedProductEntity.value!!.price, ProductListFragment.selectedProductEntity.value!!.offer)))
                FindNumberOfCartItems.productCount.value =
                    FindNumberOfCartItems.productCount.value!! - 1
                resetBadge(badgeDrawable, productDetailToolBar)
                addRemoveLayout.visibility = View.GONE
                addProductButton.visibility = View.VISIBLE
            }
            ProductListFragment.selectedProductEntity.value?.let {
                if(it.availableItems>countOfOneProduct){
                    view.findViewById<TextView>(R.id.outOfStockInProductDetail).visibility = View.GONE
                    addProductImgButton.visibility = View.VISIBLE
                }
            }
        }
        productDetailViewModel.isWishListChecked.observe(viewLifecycleOwner){
            if(it){
                productDetailToolBar.menu.findItem(R.id.addToWishlist).setVisible(false)
                productDetailToolBar.menu.findItem(R.id.addedInWishlist).setVisible(true)
            }
            else{
                productDetailToolBar.menu.findItem(R.id.addedInWishlist).setVisible(false)
                productDetailToolBar.menu.findItem(R.id.addToWishlist).setVisible(true)
            }
        }
        FindNumberOfCartItems.productCount.observe(viewLifecycleOwner){
            if(FindNumberOfCartItems.productCount.value==0){
                badgeDrawable.isVisible = false
            }
            else{
                badgeDrawable.isVisible = true
                badgeDrawable.text = FindNumberOfCartItems.productCount.value.toString()
            }
            BadgeUtils.attachBadgeDrawable(badgeDrawable,productDetailToolBar,R.id.cart)
        }
        return view
    }

    @OptIn(ExperimentalBadgeUtils::class)
    private fun resetBadge(badgeDrawable: BadgeDrawable,productDetailToolBar:MaterialToolbar) {
        if(FindNumberOfCartItems.productCount.value==0){
            badgeDrawable.isVisible = false
        }
        else{
            badgeDrawable.isVisible = true
            badgeDrawable.text = FindNumberOfCartItems.productCount.value.toString()
        }
        BadgeUtils.attachBadgeDrawable(badgeDrawable,productDetailToolBar,R.id.cart)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(isAdded) {
                    setProductValue()
                    parentFragmentManager.popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)
    }

    override fun onResume() {
        super.onResume()
        ProductListFragment.selectedProductEntity.observe(viewLifecycleOwner) { selectedProduct ->
            if(selectedProductEntityInClass==null){
                selectedProductEntityInClass = selectedProduct
            }
            if(once==0) {
                selectedProductEntityList.add(selectedProduct)
            }
            if((oneTimeFragmentIn==0) || (backNavigated) || (MainActivity.isRetailer)) {
                if(!MainActivity.isRetailer) {
                    productDetailViewModel.getSpecificProductWishList(
                        MainActivity.userId.toInt(),
                        selectedProduct.productId
                    )
                }
                productDetailToolBar.title = selectedProduct.productName
                view?.findViewById<TextView>(R.id.productDescriptionProductDetail)?.text =
                    selectedProduct.productDescription
                productDetailViewModel.getImagesForProducts(selectedProduct.productId)
                view?.findViewById<TextView>(R.id.productQuantity)?.text = ProductListFragment.selectedProductEntity.value?.productQuantity
                val productNameWithQuantity =
                    "${ProductListFragment.selectedProductEntity.value?.productName}"
                view?.findViewById<TextView>(R.id.productNameProductDetail)?.text =
                    productNameWithQuantity
                productDetailViewModel.brandName.observe(viewLifecycleOwner){
                    view?.findViewById<TextView>(R.id.productNameProductDetail)?.text = "$it $productNameWithQuantity"
                }
                var price = ""
                if ((ProductListFragment.selectedProductEntity.value?.offer ?: -1f) > 0f) {
                    mrpTextView.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
                    mrpTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    mrpTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.strikenColor))
                    val discountedPriceStr = "₹${
                        productDetailViewModel.calculateDiscountPrice(
                            ProductListFragment.selectedProductEntity.value!!.price,
                            ProductListFragment.selectedProductEntity.value!!.offer
                        )
                    }"
                    discountedPrice.visibility = View.VISIBLE
                    discountedPrice.text = discountedPriceStr
                } else {
                    mrpTextView.paintFlags = 0
                    discountedPrice.visibility = View.GONE
                }
                price = "₹${ProductListFragment.selectedProductEntity.value?.price}"
                ProductListFragment.selectedProductEntity.value?.brandId?.let {
                    productDetailViewModel.getBrandName(it)
                }
                mrpTextView.text = price
                val offerView = view?.findViewById<TextView>(R.id.productOffer)
                if ((ProductListFragment.selectedProductEntity.value?.offer ?: -1f) < 1f) {
                    offerView?.visibility = View.GONE
                } else {
                    offerView?.visibility = View.VISIBLE
                }
                var offerStr =
                    ProductListFragment.selectedProductEntity.value?.offer?.toInt().toString() + "% Off"
                offerView?.text = offerStr
                if(ProductListFragment.selectedProductEntity.value?.expiryDate?.isNotEmpty()==true) {
                    var expiry = DateGenerator.getDayAndMonth(ProductListFragment.selectedProductEntity.value?.expiryDate!!)
                    view?.findViewById<TextView>(R.id.expiryDateProductDetail)?.apply {
                        text = "Expiry: $expiry"
                    }
                }
                else{
                    view?.findViewById<TextView>(R.id.expiryDateProductDetail)?.text = "Expiry: No Expiry"
                }
                var manufactureDate = DateGenerator.getDayAndMonth(ProductListFragment.selectedProductEntity.value?.manufactureDate!!)
                view?.findViewById<TextView>(R.id.manufactureDateProductDetail)?.text = "Manufactured: $manufactureDate"

                if (ProductListFragment.selectedProductEntity.value != null) {
                    productDetailViewModel.getCartForSpecificProduct(
                        MainActivity.cartId,
                        ProductListFragment.selectedProductEntity.value!!.productId.toInt()
                    )
                    productDetailViewModel.isCartEntityAvailable.observe(viewLifecycleOwner) {
                        if (it == null) {
                            countOfOneProduct = 0
                            addRemoveLayout.visibility = View.GONE
                            addProductButton.visibility = View.VISIBLE
                        } else {
                            addRemoveLayout.visibility = View.VISIBLE
                            addProductButton.visibility = View.GONE
                            countOfOneProduct = it.totalItems
                            totalItemsAddedProductDetail.text = countOfOneProduct.toString()
                        }
                        if(ProductListFragment.selectedProductEntity.value?.availableItems==0){
                            addProductButton.visibility = View.GONE
                        }
                        else{
                            ProductListFragment.selectedProductEntity.value?.let {
                                if(it.availableItems<=countOfOneProduct){
                                    addProductImgButton.visibility = View.INVISIBLE
                                    view?.findViewById<TextView>(R.id.outOfStockInProductDetail)?.visibility = View.VISIBLE

                                }
                            }
                        }
                    }
                }
                if(backNavigated){
                    backNavigated = false
                }
                else{
                    oneTimeFragmentIn = 1
                }
            }
            once = 1
            productDetailViewModel.getSimilarProduct(selectedProduct.categoryName)
            view?.findViewById<MaterialButton>(R.id.categoryButton)?.setOnClickListener {
                var productListFragment = ProductListFragment()
                productListFragment.arguments = Bundle().apply {
                    putString("category", selectedProduct.categoryName)
                }
                FragmentTransaction.navigateWithBackstack(parentFragmentManager, productListFragment,selectedProduct.categoryName)
            }
        }

        InitialFragment.hideBottomNav.value = true
        InitialFragment.hideSearchBar.value = true
    }

    override fun onStop() {
        super.onStop()
        backNavigated = true
        recyclerView.stopScroll()
        InitialFragment.hideBottomNav.value = false
        InitialFragment.hideSearchBar.value = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
        productDetailViewModel.brandName.value = null
        productDetailViewModel.brandName.removeObservers(viewLifecycleOwner)
        productDetailViewModel.isCartEntityAvailable.removeObservers(viewLifecycleOwner)
    }

    override fun onPause() {
        super.onPause()
        productObserved = 0
        recyclerView.adapter?.let {
            it.notifyDataSetChanged()
        }
    }


    fun setProductValue(){
        val size = selectedProductEntityList.size
        try{
            selectedProductEntityList.removeAt(size-1)
            ProductListFragment.selectedProductEntity.value = selectedProductEntityList[size-2]
        }
        catch (e:Exception){
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}