package com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail



import androidx.fragment.app.Fragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.AddressRepository
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.products.CartWithProductData
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
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.TimeSlots
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.imagehandlers.SetProductImage
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary.OrderSummaryFragment
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        if(!MainActivity.isRetailer) {
            changeSubscription.visibility = View.VISIBLE
            val orderSummary = OrderSummaryFragment()
            orderSummary.arguments = Bundle().apply {
                OrderListFragment.selectedOrder?.cartId?.let {
                    putInt("cartId",it)
                }
                OrderListFragment.selectedOrder?.addressId?.let {
                    putInt("selectedAddressId",it)
                }
                OrderListFragment.selectedOrder?.orderId?.let {
                    putInt("orderId",it)
                }
            }
            changeSubscription.setOnClickListener {
                FragmentTransaction.navigateWithBackstack(parentFragmentManager,orderSummary,"Edit Order")
            }
        }
        setUpViewModel()

        val deliveryFrequency = view.findViewById<TextView>(R.id.productDeliveryFrequency)

        view.findViewById<TextView>(R.id.productOrderedDate).text = DateGenerator.getDayAndMonth(
            OrderListFragment.selectedOrder?.orderedDate?: DateGenerator.getCurrentDate())
        val deliveryDate = OrderListFragment.selectedOrder?.deliveryDate
        val deliveryText = view.findViewById<TextView>(R.id.productDeliveredDate)
        deleteSubscription = view.findViewById<MaterialButton>(R.id.deleteSubscriptionOrder)
        val deliveryTimeSlot = view.findViewById<TextView>(R.id.productNextDeliveryTimeSlot)
        val nextDeliveryDate = view.findViewById<TextView>(R.id.productNextDeliveryDate)
        val hideCancelOrderButton = arguments?.getBoolean("hideCancelOrderButton")
        if(arguments?.getBoolean("hideToolBar")==true){
            view.findViewById<MaterialToolbar>(R.id.materialToolbarOrderDetail).visibility = View.GONE
        }

        view.findViewById<MaterialToolbar>(R.id.materialToolbarOrderDetail).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }


        val deliveryStatusText = orderDetailViewModel.assignDeliveryStatus(deliveryDate)
        deliveryStatusText?.let {
            deliveryText.text = it
        }
        if(orderDetailViewModel.assignDeliveryStatus(deliveryDate)==null){
            deliveryText.visibility = View.GONE
        }

        orderDetailViewModel.getSelectedAddress(OrderListFragment.selectedOrder!!.addressId)
        orderDetailViewModel.selectedAddress.observe(viewLifecycleOwner){ address ->
            view.findViewById<TextView>(R.id.addressOwnerName).text = address.addressContactName
            val addressText = with(address){
                "$buildingName, $streetName, $city, $state, $postalCode"
            }
            view.findViewById<TextView>(R.id.address).text = addressText
            view.findViewById<TextView>(R.id.addressPhone).text = address.addressContactNumber

        }
        view.findViewById<TextView>(R.id.orderIdValue).text = OrderListFragment.selectedOrder?.orderId.toString()
        view.findViewById<TextView>(R.id.productDeliveredStatus).text = OrderListFragment.selectedOrder?.deliveryStatus
        deliveryFrequency.text = OrderListFragment.selectedOrder?.deliveryFrequency


        if(OrderListFragment.selectedOrder?.deliveryFrequency!="Once"){
            deleteSubscription.text = "Stop Subscription"
            alertTitle = "Stop Subscription!!"
            alertMessage = "Are you Sure to Stop the Subscription?"
            nextDeliveryDate.visibility = View.VISIBLE
            deliveryTimeSlot.visibility = View.VISIBLE
            deliveryText.visibility =View.GONE
            OrderListFragment.selectedOrder?.let {
                orderDetailViewModel.getTimeSlot(it.orderId)
            }
            if(OrderListFragment.selectedOrder?.deliveryFrequency=="Daily"){
                var text = "Next Delivery on ${DateGenerator.getDayAndMonth(DateGenerator.getDeliveryDate())}"
                nextDeliveryDate.text = text
            }
            when(OrderListFragment.selectedOrder?.deliveryFrequency){
                "Monthly Once" -> {
                    OrderListFragment.selectedOrder?.let {
                        orderDetailViewModel.getMonthlySubscriptionDate(it.orderId)
                    }
                }
                "Weekly Once" -> {
                    OrderListFragment.selectedOrder?.let {
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
            OrderListFragment.selectedOrder?.let {
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
                    if(currentTime in 6..8) {
                        if (OrderListFragment.selectedOrder?.deliveryFrequency == "Daily") {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                }
                1 -> {
                    text = TimeSlots.MID_MORNING.timeDetails
                    if(currentTime in 8..14) {
                        if (OrderListFragment.selectedOrder?.deliveryFrequency == "Daily") {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                }
                2 -> {
                    text = TimeSlots.AFTERNOON.timeDetails
                    if(currentTime in 14..18) {
                        if (OrderListFragment.selectedOrder?.deliveryFrequency == "Daily") {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                }
                3 -> {
                    text = TimeSlots.EVENING.timeDetails
                    if(currentTime in 18..20) {
                        if (OrderListFragment.selectedOrder?.deliveryFrequency == "Daily") {
                            nextDeliveryDate.text = groceriesArrivingToday
                        }
                    }
                }
            }
            text = "Time Slot: $text"
            deliveryTimeSlot.text = text
            isTimeSlotAvailable.value = it
        }
        isTimeSlotAvailable.observe(viewLifecycleOwner) { timeSlot ->
            orderDetailViewModel.date.observe(viewLifecycleOwner) {
                val currentTime = DateGenerator.getCurrentTime()
                var text = "Next Delivery on "
                if (OrderListFragment.selectedOrder?.deliveryFrequency == "Weekly Once") {
                    if (DateGenerator.getCurrentDay() == days[it]) {
                        text = orderDetailViewModel.assignText(timeSlot,currentTime)
                    } else {
                        text = "Next Delivery this "
                        text += days[it]
                    }
                } else if (OrderListFragment.selectedOrder?.deliveryFrequency == "Monthly Once") {
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
                            text = orderDetailViewModel.assignText(timeSlot,currentTime)
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
        for(i in OrderListFragment.correspondingCartList!!){
            addView(productsContainer,i)
            totalItems++
        }
        val totalItemsStr = "MRP ($totalItems Products)"
        view.findViewById<TextView>(R.id.priceDetailsMrpTotalItems).text = totalItemsStr
        val totalPriceStr = "₹${totalPrice}"
        val grandTotal = "₹${totalPrice+49}"
        view.findViewById<TextView>(R.id.priceDetailsMrpPrice).text = totalPriceStr
        view.findViewById<TextView>(R.id.priceDetailsTotalAmount).text = grandTotal
        setUpDeleteSubscriptionListeners()


        if((MainActivity.isRetailer) || (OrderListFragment.selectedOrder?.deliveryStatus=="Cancelled") || (OrderListFragment.selectedOrder?.deliveryStatus=="Delivered") || (hideCancelOrderButton==true)){
            deleteSubscription.visibility = View.GONE
            changeSubscription.visibility = View.GONE
        }
        if(!(MainActivity.isRetailer) && (OrderListFragment.selectedOrder?.deliveryFrequency!="Once") && (hideCancelOrderButton!=true)) {
            changeSubscription.visibility = View.VISIBLE
        }
        else{
            changeSubscription.visibility = View.GONE
        }
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
                    OrderListFragment.selectedOrder?.let {
                        orderDetailViewModel.updateOrderDetails(it.copy(deliveryFrequency = "Once", deliveryStatus = "Cancelled"))
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


    private fun addView(container:LinearLayout,productInfo: CartWithProductData){
        val newView =LayoutInflater.from(requireContext()).inflate(R.layout.ordered_product_layout,container,false)
        newView.findViewById<ImageView>(R.id.orderedProductImage)
        SetProductImage.setImageView(newView.findViewById(R.id.orderedProductImage),productInfo.mainImage?:"",
            File(requireContext().filesDir,"AppImages")
        )
        val eachPriceText = newView.findViewById<TextView>(R.id.orderedEachProductPrice)
        newView.findViewById<TextView>(R.id.orderedProductFullName).text = productInfo.productName
        newView.findViewById<TextView>(R.id.orderedProductQuantity).text = productInfo.productQuantity
        newView.findViewById<TextView>(R.id.orderedProductBrandName).text = productInfo.brandName
        totalPrice += (productInfo.totalItems*productInfo.unitPrice)
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
        InitialFragment.hideSearchBar.value = true
        InitialFragment.hideBottomNav.value = true
    }
    override fun onStop() {
        super.onStop()
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }

}