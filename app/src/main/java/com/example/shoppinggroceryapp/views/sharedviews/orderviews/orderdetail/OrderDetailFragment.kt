package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail



import androidx.fragment.app.Fragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.core.domain.order.OrderDetails
import com.core.domain.products.CartWithProductData
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.TimeSlots
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.imagehandlers.SetProductImage
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary.OrderSummaryFragment
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail.ProductDetailFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.userviews.cartview.FindNumberOfCartItems
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.PaymentFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.io.File


class OrderDetailFragment : Fragment() {

    var days = listOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
    var groceriesArrivingToday = "Groceries Arriving Today"
    var isTimeSlotAvailable:MutableLiveData<Int> = MutableLiveData()
    private lateinit var deleteSubscription:MaterialButton
    var alertTitle = ""
    var alertMessage = ""
    private lateinit var orderDetailViewModel:OrderDetailViewModel
    private var totalPrice = 0f
    private var selectedOrder:OrderDetails? = null
    var status:MutableLiveData<String> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_detail, container, false)
        val productsContainer = view.findViewById<LinearLayout>(R.id.orderedProductViews)
        val changeSubscription = view.findViewById<MaterialButton>(R.id.modifySubscriptionOrder)
        val mrpPrice = view.findViewById<TextView>(R.id.priceDetailsMrpPrice)
        val grandTot = view.findViewById<TextView>(R.id.priceDetailsTotalAmount)
        val isRestartApp  = arguments?.getBoolean("restartApp")==true
        selectedOrder = arguments?.let {
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
        if(!MainActivity.isRetailer) {
            changeSubscription.visibility = View.VISIBLE
            val orderSummary = OrderSummaryFragment()
            orderSummary.arguments = Bundle().apply {
                selectedOrder?.let {
                    this.putInt("orderId",it.orderId)
                    this.putInt("cartId",it.cartId)
                    this.putInt("addressId",it.addressId)
                    this.putString("paymentMode",it.paymentMode)
                    this.putString("deliveryFrequency",it.deliveryFrequency)
                    this.putString("paymentStatus",it.paymentStatus)
                    this.putString("deliveryStatus",it.deliveryStatus)
                    this.putString("deliveryDate",it.deliveryDate)
                    this.putString("orderedDate",it.orderedDate)
                }

                selectedOrder?.cartId?.let {
                    putInt("cartId",it)
                }
                selectedOrder?.addressId?.let {
                    putInt("selectedAddressId",it)
                }
                selectedOrder?.orderId?.let {
                    putInt("orderId",it)
                }

            }
            changeSubscription.setOnClickListener {
                FragmentTransaction.navigateWithBackstack(parentFragmentManager,orderSummary,"Order Summary Fragment")
            }
        }
        setUpViewModel()

        val deliveryFrequency = view.findViewById<TextView>(R.id.productDeliveryFrequency)

        view.findViewById<TextView>(R.id.productOrderedDate).text = DateGenerator.getDayAndMonth(
            selectedOrder?.orderedDate?: DateGenerator.getCurrentDate())
        val deliveryDate = selectedOrder?.deliveryDate
        val deliveryText = view.findViewById<TextView>(R.id.productDeliveredDate)
        deleteSubscription = view.findViewById(R.id.deleteSubscriptionOrder)
        val deliveryTimeSlot = view.findViewById<TextView>(R.id.productNextDeliveryTimeSlot)
        val nextDeliveryDate = view.findViewById<TextView>(R.id.productNextDeliveryDate)
        val hideCancelOrderButton = arguments?.getBoolean("hideCancelOrderButton")
        val isOrderedProducts = arguments?.getBoolean("hideToolBar")
        if(isOrderedProducts==true){
            view.findViewById<MaterialToolbar>(R.id.materialToolbarOrderDetail).visibility = View.GONE
            view.findViewById<TextView>(R.id.orderId).apply {
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium)
                setTextColor(ContextCompat.getColor(requireContext(),R.color.strikenColor))
                setTypeface(this.typeface,Typeface.BOLD)
            }
            view.findViewById<TextView>(R.id.orderIdValue).apply {
                setTypeface(this.typeface,Typeface.BOLD)
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium)
                setTextColor(ContextCompat.getColor(requireContext(),R.color.strikenColor))
            }

        }

        view.findViewById<MaterialToolbar>(R.id.materialToolbarOrderDetail).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }


        val deliveryStatusText = orderDetailViewModel.assignDeliveryStatus(deliveryDate,selectedOrder)
        deliveryStatusText?.let {
            deliveryText.text = it
        }
        if(orderDetailViewModel.assignDeliveryStatus(deliveryDate,selectedOrder)==null){
            deliveryText.visibility = View.GONE
        }

        orderDetailViewModel.getSelectedAddress(selectedOrder!!.addressId)
        orderDetailViewModel.selectedAddress.observe(viewLifecycleOwner){ address ->
            view.findViewById<TextView>(R.id.addressOwnerName).text = address.addressContactName
            val addressText = with(address){
                "$buildingName, $streetName, $city, $state, $postalCode"
            }
            view.findViewById<TextView>(R.id.address).text = addressText
            view.findViewById<TextView>(R.id.addressPhone).text = address.addressContactNumber

        }
        view.findViewById<TextView>(R.id.orderIdValue).text = selectedOrder?.orderId.toString()
        view.findViewById<TextView>(R.id.productDeliveredStatus).text = selectedOrder?.deliveryStatus
        deliveryFrequency.text = selectedOrder?.deliveryFrequency

        if(selectedOrder?.deliveryFrequency!="Once"){
            deleteSubscription.text = "Stop Subscription"
            alertTitle = "Stop Subscription!!"
            alertMessage = "Are you Sure to Stop the Subscription?"
            nextDeliveryDate.visibility = View.VISIBLE
            deliveryTimeSlot.visibility = View.VISIBLE
            deliveryText.visibility =View.GONE
            selectedOrder?.let {
                orderDetailViewModel.getTimeSlot(it.orderId)
            }
            if(selectedOrder?.deliveryFrequency=="Daily"){
                var text = "Next Delivery on ${DateGenerator.getDayAndMonth(DateGenerator.getDeliveryDate())}"
                nextDeliveryDate.text = text
            }
            when(selectedOrder?.deliveryFrequency){
                "Monthly Once" -> {
                    selectedOrder?.let {
                        orderDetailViewModel.getMonthlySubscriptionDate(it.orderId)
                    }
                }
                "Weekly Once" -> {
                    selectedOrder?.let {
                        orderDetailViewModel.getWeeklySubscriptionDate(it.orderId)
                    }
                }
            }
        }
        else{
            alertTitle = "Cancel Order!!"
            alertMessage = "Are you Sure to Cancel the Order?"
            deleteSubscription.text = "Cancel Order"
            nextDeliveryDate.visibility = View.GONE
            deliveryTimeSlot.visibility = View.GONE
            selectedOrder?.let {
                if(it.deliveryFrequency=="Cancelled"){
                    deliveryText.visibility = View.GONE
                }
            }
        }
        orderDetailViewModel.timeSlot.observe(viewLifecycleOwner){
            var text = ""
            var currentTime = DateGenerator.getCurrentTime()
            when(it){
                0 -> {
                    text = TimeSlots.EARLY_MORNING.timeDetails
                    if((currentTime in 6..8)) {
                        if (selectedOrder?.deliveryFrequency == "Daily" && selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                    else if(currentTime<8){
                        if (selectedOrder?.deliveryFrequency == "Daily"&& selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }

                }
                1 -> {
                    text = TimeSlots.MID_MORNING.timeDetails

                    if(currentTime in 8..14) {
                        if (selectedOrder?.deliveryFrequency == "Daily" && selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                    else if(currentTime<14){
                        if (selectedOrder?.deliveryFrequency == "Daily"&& selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }

                }
                2 -> {
                    text = TimeSlots.AFTERNOON.timeDetails
                    if(currentTime in 14..18) {
                        if (selectedOrder?.deliveryFrequency == "Daily"&& selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                    else if(currentTime<18){
                        if (selectedOrder?.deliveryFrequency == "Daily"&& selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }

                }
                3 -> {
                    text = TimeSlots.EVENING.timeDetails
                    if(currentTime in 18..20) {
                        if (selectedOrder?.deliveryFrequency == "Daily"&& selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                    else if(currentTime<20){
                        if (selectedOrder?.deliveryFrequency == "Daily"&& selectedOrder?.orderedDate!=DateGenerator.getCurrentDate()) {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                }
            }
            text = "Time Slot: $text"
            deliveryTimeSlot.text = text
            isTimeSlotAvailable.value = it
        }
        orderDetailViewModel.selectedOrderProduct = MutableLiveData()
        isTimeSlotAvailable.observe(viewLifecycleOwner) { timeSlot ->
            orderDetailViewModel.date.observe(viewLifecycleOwner) {
                val currentTime = DateGenerator.getCurrentTime()
                var text = "Next Delivery on "
                if (selectedOrder?.deliveryFrequency == "Weekly Once") {
                    if (DateGenerator.getCurrentDay() == days[it]) {
                        text = orderDetailViewModel.assignText(timeSlot,currentTime,selectedOrder)?:"Next Delivery on Next ${days[it]}"
                    } else {
                        text = "Next Delivery this "
                        text += days[it]
                    }
                } else if (selectedOrder?.deliveryFrequency == "Monthly Once") {
                    var currentDay = DateGenerator.getCurrentDayOfMonth()
                    try {
                        if (currentDay.toInt() > it) {
                            text = "Next Delivery on ${
                                DateGenerator.getDayAndMonth(
                                    DateGenerator.getNextMonth().substring(0, 8) + it
                                )
                            }"
                        }
                        else if (currentDay.toInt() == it) {
                            text = orderDetailViewModel.assignText(timeSlot,currentTime,selectedOrder)?:"Next Delivery on ${
                                DateGenerator.getDayAndMonth(
                                    DateGenerator.getCurrentDate().substring(0, 8) + it
                                )
                            }"
                        }
                        else {
                            text = "Next Delivery on ${
                                DateGenerator.getDayAndMonth(
                                    DateGenerator.getCurrentDate().substring(0, 8) + it
                                )
                            }"
                        }
                    } catch (e: Exception) {
                    }

                }
                nextDeliveryDate.text = text
            }
        }

        var totalItems = 0
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
        var isDeleted = false
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
                isDeleted = it.getBoolean("isDeleted$i")
            }
            if(manufactureDateI==null && mainImageI==null && productQuantityI==null && productNameI==null && descriptionI==null && expiryDateI==null && brandNameI==null){
                break
            }
            val cartWithOrderData = CartWithProductData(productId,mainImageI,productNameI?:"",descriptionI?:"",totalItemsI,unitPriceI,manufactureDateI?:"",expiryDateI?:"",productQuantityI?:"",brandNameI?:"")
            orderDetailViewModel.selectedOrderWithProductData.add(cartWithOrderData)
            addView(productsContainer,cartWithOrderData,isDeleted)
            if(!isDeleted) {
                totalItems++
            }
            i++
        }
//        for(i in OrderListFragment.correspondingCartList!!){
//            addView(productsContainer,i)
//            totalItems++
//        }
        val totalItemsStr = "MRP ($totalItems Products)"
        view.findViewById<TextView>(R.id.priceDetailsMrpTotalItems).text = totalItemsStr
        val totalPriceStr = "₹${totalPrice}"
        val grandTotal = "₹${totalPrice+49}"
        if(totalPrice==0f){
            view.findViewById<LinearLayout>(R.id.linearLayout12).visibility = View.GONE
        }
        mrpPrice.text = totalPriceStr
        grandTot.text = grandTotal
        setUpDeleteSubscriptionListeners()


        if((MainActivity.isRetailer) || (selectedOrder?.deliveryStatus=="Cancelled") || (selectedOrder?.deliveryStatus=="Delivered") || (hideCancelOrderButton==true)){
            deleteSubscription.visibility = View.GONE
            changeSubscription.visibility = View.GONE
        }
        if(!(MainActivity.isRetailer) && ((selectedOrder?.deliveryFrequency!="Once") &&  (selectedOrder?.deliveryStatus!="Cancelled")) && (hideCancelOrderButton!=true)) {
            changeSubscription.visibility = View.VISIBLE
        }
        else{
            changeSubscription.visibility = View.GONE
        }
        if(selectedOrder?.deliveryStatus=="Cancelled"){
            view.findViewById<LinearLayout>(R.id.deliveryStatus).visibility = View.VISIBLE
        }
        else{
            view.findViewById<LinearLayout>(R.id.deliveryStatus).visibility = View.GONE
        }
        orderDetailViewModel.selectedOrderProduct.observe(viewLifecycleOwner){
            println("989891  in observer  value $it")
            if(it!=null) {
                ProductListFragment.selectedProductEntity.value = it
                FragmentTransaction.navigateWithBackstack(
                    parentFragmentManager,
                    ProductDetailFragment(),
                    "product Detail Fragment"
                )
            }
            else{
                ShowShortToast.show("Product has been removed from inventory",requireContext())
            }
        }
        val isOrderedProduct = arguments?.getBoolean("hideButtons")
        if(isOrderedProduct==true){
            deleteSubscription.visibility = View.GONE
            changeSubscription.visibility = View.GONE
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                println("32432 ON ORDER SUCCESS FRAGMENT BACKUP value: $isOrderedProducts $isRestartApp")
                if(isRestartApp){
                    restartApp()
                }
                else {
                    parentFragmentManager.popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)

        return view
    }

    private fun setUpViewModel() {
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        orderDetailViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(
                retailerDao, userDao)
        )[OrderDetailViewModel::class.java]
    }

    private fun setUpDeleteSubscriptionListeners() {
        deleteSubscription.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(alertTitle)
                .setMessage(alertMessage)
                .setPositiveButton("Yes"){dialog,_->
                    dialog.dismiss()
                    selectedOrder?.let {
                        orderDetailViewModel.updateOrderDetails(it.copy(deliveryStatus = "Cancelled"))
                        when(it.deliveryFrequency){
                            "Monthly Once" -> {orderDetailViewModel.deleteMonthly(it.orderId)}
                            "Weekly Once" -> {orderDetailViewModel.deleteWeekly(it.orderId)}
                            "Daily" -> {orderDetailViewModel.deleteDaily(it.orderId)}
                        }
                    }
                    parentFragmentManager.popBackStack()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

    }


    private fun addView(container:LinearLayout,productInfo: CartWithProductData,addDeleteTag:Boolean){
        val newView =LayoutInflater.from(requireContext()).inflate(R.layout.ordered_product_layout,container,false)
        newView.findViewById<ImageView>(R.id.orderedProductImage)
        newView.setOnClickListener {
            orderDetailViewModel.getProductById(productInfo.productId)
        }
        lifecycleScope.launch {
            SetProductImage.setImageView(newView.findViewById(R.id.orderedProductImage),productInfo.mainImage?:"",
                File(requireContext().filesDir,"AppImages")
            )
        }

        val eachPriceText = newView.findViewById<TextView>(R.id.orderedEachProductPrice)
        newView.findViewById<TextView>(R.id.orderedProductFullName).text = productInfo.productName
        newView.findViewById<TextView>(R.id.orderedProductQuantity).text = productInfo.productQuantity
        newView.findViewById<TextView>(R.id.orderedProductBrandName).text = productInfo.brandName
        if(addDeleteTag){
            newView.findViewById<TextView>(R.id.productDeletedTag).visibility = View.VISIBLE
        }
        else{
            newView.findViewById<TextView>(R.id.productDeletedTag).visibility = View.GONE
            totalPrice += (productInfo.totalItems*productInfo.unitPrice)
        }
        val totalPrice = "₹${(productInfo.totalItems*productInfo.unitPrice)}"
        newView.findViewById<TextView>(R.id.orderedProductTotalPrice).text = totalPrice
        val eachPrice = "₹${(productInfo.unitPrice)}"
        eachPriceText.text = eachPrice
        val str = "(${productInfo.totalItems})"
        newView.findViewById<TextView>(R.id.orderedNoOfProducts).text =str
        if(productInfo.totalItems==1){
            newView.findViewById<TextView>(R.id.eachTextViewOrderDetail).visibility = View.INVISIBLE
            eachPriceText.visibility = View.INVISIBLE
        }
        container.addView(newView)
    }

    override fun onResume() {
        super.onResume()
        println("32432 on resume order detail")
        InitialFragment.hideSearchBar.value = true
        InitialFragment.hideBottomNav.value = true
    }
    override fun onStop() {
        super.onStop()
        println("32432 on stop order detail")
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }

    override fun onDestroy() {
        super.onDestroy()
        println("32432 on destroy order detail")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("32432 on destroy view order detail")
    }
    private fun restartApp() {
        PaymentFragment.paymentMode =""
        FindNumberOfCartItems.productCount.value = 0
        parentFragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        val intent = Intent(context,MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)
//        requireActivity().finish()
    }
}