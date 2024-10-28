package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersummary

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.OrderDetails
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
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
import com.example.shoppinggroceryapp.helpers.alertdialog.DataLossAlertDialog
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.helpers.snackbar.ShowShortSnackBar
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.adapter.ProductViewAdapter
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressList
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.PaymentFragment
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.TimeSlots
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.io.File


class OrderSummaryFragment : Fragment() {


    var once = false
    var weeklyOnce = false
    var daily = false
    var monthlyOnce = false
    var tmpCart:Int? = null
    var tmpAddress:Int? = null
    private lateinit var orderSummaryViewModel:OrderSummaryViewModel
    private lateinit var continueToPayment:MaterialButton
    var tmpOrderId:Int? = null
    var isEditing = false
    private lateinit var viewProductDetails:MaterialButton
    private lateinit var deliveryFrequency:MaterialAutoCompleteTextView
    private lateinit var radioGroupTimeSlot:RadioGroup
    private lateinit var deliveryFrequencyDay:MaterialAutoCompleteTextView
    private lateinit var noteForUserLayout: LinearLayout
    private lateinit var dayOfMonth:MaterialAutoCompleteTextView
    private lateinit var deliveryDate:TextView
    private var selectedOrder:OrderDetails? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val totalItems = arguments?.getInt("noOfItems")
        val days = arrayOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
        val daysOfMonth = arrayOf("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28")
        val view =  inflater.inflate(R.layout.fragment_order_summary, container, false)
        val dayOfMonthLayout = view.findViewById<LinearLayout>(R.id.deliveryFrequencyDayMonthLayout)
        val noteForUser = view.findViewById<TextView>(R.id.noteForUser)
        dayOfMonth = view.findViewById(R.id.deliveryFrequencyDayMonth)
        val addressOwnerName = view.findViewById<TextView>(R.id.addressOwnerNameOrderSummary)
        radioGroupTimeSlot = view.findViewById(R.id.radioGroupTimeSlot)
        val addressValue = view.findViewById<TextView>(R.id.addressOrderSummary)
        val addressNumber = view.findViewById<TextView>(R.id.addressPhoneOrderSummary)
        val changeAddressButton = view.findViewById<MaterialButton>(R.id.changeAddressButtonOrderSummary)
        val noOfItems = view.findViewById<TextView>(R.id.priceDetailsMrpTotalItemsOrderSummary)
        val mrpPrice = view.findViewById<TextView>(R.id.priceDetailsMrpPriceOrderSummary)
        val totalAmount = view.findViewById<TextView>(R.id.priceDetailsTotalAmountOrderSummary)
        continueToPayment = view.findViewById(R.id.continueButtonOrderSummary)
        viewProductDetails = view.findViewById(R.id.viewPriceDetailsButtonOrderSummary)
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
        val orderSummaryToolBar = view.findViewById<MaterialToolbar>(R.id.orderSummaryToolbar)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        orderSummaryViewModel = ViewModelProvider(this,
            GroceryAppUserVMFactory(userDao, retailerDao)
        )[OrderSummaryViewModel::class.java]
        val recyclerViewProducts = view.findViewById<RecyclerView>(R.id.orderListRecyclerView)
        deliveryFrequency = view.findViewById(R.id.deliveryFrequency)
        deliveryFrequencyDay = view.findViewById(R.id.deliveryFrequencyDay)
        val scrollView = view.findViewById<ScrollView>(R.id.orderSummaryScrollView)
        deliveryDate = view.findViewById<TextView>(R.id.textView)
        noteForUserLayout = view.findViewById(R.id.noteForUserLayout)
        val deliveryFrequencyDayLayout = view.findViewById<LinearLayout>(R.id.deliveryFrequencyDayLayout)
        val timeSlotLayout = view.findViewById<LinearLayout>(R.id.timeSlotLayout)

        tmpCart = arguments?.getInt("cartId")
        tmpAddress = arguments?.getInt("selectedAddressId")
        tmpOrderId = arguments?.getInt("orderId")
        tmpCart?.let {
            if(it!=0) {
                orderSummaryViewModel.getProductsWithCartId(cartId = it)
            }
        }

        deliveryDate.text = orderSummaryViewModel.getExpectedDeliveryDate("once","1")

        tmpAddress?.let {
            if(it!=0) {
                view.findViewById<LinearLayout>(R.id.deliveryAddressLayoutOrderSummary).visibility =
                    View.GONE

                isEditing = true
                continueToPayment.text = "Update Order"
                deliveryDate.visibility = View.GONE
                view.findViewById<LinearLayout>(R.id.priceDetailsOrderSummary).visibility =
                    View.GONE
                view.findViewById<LinearLayout>(R.id.linearLayout11)
                    .setBackgroundColor(Color.TRANSPARENT)
                viewProductDetails.visibility = View.GONE
            }
        }
        if(tmpCart==0) {
            orderSummaryViewModel.getProductsWithCartId(cartId = MainActivity.cartId)
        }
        orderSummaryViewModel.cartItems.observe(viewLifecycleOwner){
            ProductViewAdapter.productsList = it
            recyclerViewProducts.adapter = ProductViewAdapter(File(requireContext().filesDir,"AppImages"),this)
            recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
        val addressVal = "${CartFragment.selectedAddressEntity?.buildingName}, ${CartFragment.selectedAddressEntity?.streetName}, ${CartFragment.selectedAddressEntity?.city}, ${CartFragment.selectedAddressEntity?.state}, ${CartFragment.selectedAddressEntity?.postalCode}"
        addressOwnerName.text = CartFragment.selectedAddressEntity?.addressContactName
        addressValue.text = addressVal
        addressNumber.text = CartFragment.selectedAddressEntity?.addressContactNumber

        val items = "MRP ($totalItems Products)"
        noOfItems.text = items
        val mrpAmt = "₹${CartFragment.viewPriceDetailData.value!!-49}"
        val grandAmt = "₹${CartFragment.viewPriceDetailData.value!!}"
        val priceDetails = "$grandAmt\nView Price Details"
        viewProductDetails.text = priceDetails
        mrpPrice.text =mrpAmt
        totalAmount.text = grandAmt
        viewProductDetails.setOnClickListener {
            scrollView.fullScroll(View.FOCUS_DOWN)
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
        changeAddressButton.setOnClickListener {
            val savedAddressList = SavedAddressList()
            savedAddressList.arguments = Bundle().apply {
                putBoolean("clickable",true)
            }
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,savedAddressList,"Saved Address List Fragment")
        }

        deliveryFrequency.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(s: Editable?) {
                noteForUserLayout.visibility = View.VISIBLE
                timeSlotLayout.visibility = View.VISIBLE
                weeklyOnce = false
                once = false
                daily = false
                var noteForUserText = ""
                monthlyOnce = false
                val changedText = s.toString()
                when(changedText){
                    "Weekly Once" ->{
                        deliveryDate.visibility = View.GONE
                        noteForUserText = "Weekly deliveries kick off tomorrow! You can easily cancel your orders through your order history."
                        val newDays = days.filter { it!=DateGenerator.getCurrentDay() }.toTypedArray()
                        deliveryFrequencyDay.setSimpleItems(newDays)
                        dayOfMonth.setText("")
                        weeklyOnce = true
                        deliveryFrequencyDayLayout.visibility = View.VISIBLE
                        dayOfMonthLayout.visibility = View.GONE
                    }
                    "Monthly Once" -> {
                        deliveryDate.visibility = View.GONE
                        deliveryFrequencyDay.setText("")
                        val newDays = daysOfMonth.filter { it!=DateGenerator.getCurrentDayOfMonth() }.toTypedArray()
                        dayOfMonth.setSimpleItems(newDays)
                        noteForUserText = "Start your monthly delivery service tomorrow! You can easily cancel your orders in your order history"
                        monthlyOnce = true
                        dayOfMonthLayout.visibility = View.VISIBLE
                        deliveryFrequencyDayLayout.visibility = View.GONE
                    }
                    "Daily" ->{
                        deliveryDate.text = orderSummaryViewModel.getExpectedDeliveryDate("once","1")
                        deliveryDate.visibility = View.VISIBLE
                        deliveryFrequencyDay.setText("")
                        dayOfMonth.setText("")
                        noteForUserText = "Get ready for daily deliveries starting tomorrow! Cancel your orders anytime in the order history."
                        daily = true
                        dayOfMonthLayout.visibility = View.GONE
                        deliveryFrequencyDayLayout.visibility = View.GONE
                    }
                    else ->{
                        deliveryDate.text = orderSummaryViewModel.getExpectedDeliveryDate("once","1")
                        deliveryDate.visibility = View.VISIBLE
                        once = true
                        noteForUserLayout.visibility = View.GONE
                        deliveryFrequencyDay.setText("")
                        dayOfMonth.setText("")
                        dayOfMonthLayout.visibility = View.GONE
                        deliveryFrequencyDayLayout.visibility = View.GONE
                        timeSlotLayout.visibility = View.GONE
                    }
                }
                noteForUser.text = noteForUserText
                if(isEditing){
                    noteForUser.text = "The delivery set for today will stay the same; changes will start from the next day."
                }
            }
        })

        if(isEditing){
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    DataLossAlertDialog().showDataLossAlertDialog(requireContext(),parentFragmentManager)
                }
            })
        }


        orderSummaryToolBar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        continueToPayment.setOnClickListener {
            if(deliveryFrequency.text.isEmpty()){
                ShowShortSnackBar.showRedColor(requireView(),"Please Select the Delivery Frequency")
            }
            else {
                if(once){
                    doPaymentTransaction(null,null,null)
                }
                else if(daily){
                    checkSlotChosen(1)
                }
                else if(weeklyOnce){
                    checkSlotChosen(2)
                }
                else if(monthlyOnce){
                    checkSlotChosen(3)
                }
            }
        }
        addTextChangedListeners(deliveryFrequencyDay,"dayOfWeek")
        addTextChangedListeners(dayOfMonth,"dayOfMonth")
        return view
    }

    private fun checkSlotChosen(choice:Int?) {
        if(radioGroupTimeSlot.checkedRadioButtonId==-1){
            view?.let {
                ShowShortSnackBar.showRedColor(requireView(),"Please choose the Slot")
            }
        }
        else{
            when(choice){
                1 -> doPaymentTransaction(radioGroupTimeSlot.findViewById<RadioButton>(radioGroupTimeSlot.checkedRadioButtonId).text.toString(),null,null)
                2 -> {
                    if(deliveryFrequencyDay.text.isEmpty()){
                        ShowShortSnackBar.showRedColor(requireView(),"Please choose the Day")
                    }
                    else {
                        var weekDay: Int = -1
                        when (deliveryFrequencyDay.text.toString()) {
                            "Sunday" -> weekDay = 0
                            "Monday" -> weekDay = 1
                            "Tuesday" -> weekDay = 2
                            "Wednesday" -> weekDay = 3
                            "Thursday" -> weekDay = 4
                            "Friday" -> weekDay = 5
                            "Saturday" -> weekDay = 6
                        }
                        doPaymentTransaction(
                            radioGroupTimeSlot.findViewById<RadioButton>(
                                radioGroupTimeSlot.checkedRadioButtonId
                            ).text.toString(), null, weekDay
                        )
                    }
                }
                3 ->{
                    if(dayOfMonth.text.isEmpty()){
                        ShowShortSnackBar.showRedColor(requireView(),"Please Choose the Day of Month")
                    }
                    else{
                        try{
                            doPaymentTransaction(
                                radioGroupTimeSlot.findViewById<RadioButton>(
                                    radioGroupTimeSlot.checkedRadioButtonId
                                ).text.toString(), dayOfMonth.text.toString().toInt(), null
                            )
                        }
                        catch (e:Exception){
                            println("EXCEPTION IN ORDER SUMMARY : $e")
                        }
                    }
                }
            }
        }
    }


    private fun doPaymentTransaction(timeSlot:String?, dayOfMonth:Int?, dayOfWeek:Int?){
        val paymentFragment = PaymentFragment()
        paymentFragment.arguments =orderSummaryViewModel.putBundleValuesForOrder(timeSlot,deliveryFrequency.text.toString(),dayOfMonth, dayOfWeek)
        val timeId = orderSummaryViewModel.timeIdForOrder
        if(tmpAddress!=0 && tmpCart != 0 && tmpOrderId!=0){
            selectedOrder?.let {
                orderSummaryViewModel.updateOrderDetails(it.copy(deliveryFrequency = deliveryFrequency.text.toString()))
                orderSummaryViewModel.updateTimeSlot(TimeSlot(tmpOrderId!!,timeId))
                orderSummaryViewModel.updateSubscription(deliveryFrequency.text.toString(),tmpOrderId,dayOfMonth, dayOfWeek)
            }
            ShowShortToast.show("Delivery Subscription Updated Successfully",requireContext())
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
        }
        else {
            FragmentTransaction.navigateWithBackstack(
                parentFragmentManager,
                paymentFragment,
                "Payment Fragment"
            )
        }
    }
    override fun onResume() {
        super.onResume()
        InitialFragment.hideBottomNav.value = true
        InitialFragment.hideSearchBar.value = true
    }

    override fun onPause() {
        super.onPause()
        deliveryFrequency.setText("")
        InitialFragment.hideBottomNav.value = false
        InitialFragment.hideSearchBar.value = false
    }

    override fun onDestroy() {
        super.onDestroy()
        tmpCart = null
        tmpOrderId = null
        tmpAddress = null
    }

    private fun addTextChangedListeners(materialAutoCompleteView:MaterialAutoCompleteTextView, tag:String){

        materialAutoCompleteView.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                when(tag){
                    "dayOfMonth" -> {
                        if(s?.isNotEmpty()==true) {
                            deliveryDate.visibility = View.VISIBLE
                            deliveryDate.text = orderSummaryViewModel.getExpectedDeliveryDate(tag,s.toString())
                        }
                    }
                    "dayOfWeek" -> {
                        if(s?.isNotEmpty()==true) {
                            deliveryDate.visibility = View.VISIBLE
                            deliveryDate.text = orderSummaryViewModel.getExpectedDeliveryDate(tag,s.toString())
                        }
                    }
                }
            }
        } )
    }
}