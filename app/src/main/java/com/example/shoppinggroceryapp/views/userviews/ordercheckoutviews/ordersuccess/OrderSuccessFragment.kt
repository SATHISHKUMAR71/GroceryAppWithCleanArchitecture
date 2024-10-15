package com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.ordersuccess

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
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
import com.core.domain.order.DailySubscription
import com.core.domain.order.MonthlyOnce
import com.core.domain.order.OrderDetails
import com.core.domain.order.TimeSlot
import com.core.domain.order.WeeklyOnce
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.MainActivity.Companion.cartId
import com.example.shoppinggroceryapp.MainActivity.Companion.userId
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
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderdetail.OrderDetailFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.example.shoppinggroceryapp.views.userviews.ordercheckoutviews.PaymentFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton


class OrderSuccessFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view =  inflater.inflate(R.layout.fragment_order_confirmation, container, false)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        val orderSuccessViewModel = ViewModelProvider(this,
            GroceryAppUserVMFactory(userDao, retailerDao)
        )[OrderSuccessViewModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                restartApp()
            }
        })

        view.findViewById<MaterialToolbar>(R.id.orderSuccessToolbar).setNavigationOnClickListener {
            restartApp()
        }
        view.findViewById<MaterialButton>(R.id.materialButtonClose).setOnClickListener {
            restartApp()
        }

        val deliveryFrequency = arguments?.getString("deliveryFrequency")?:"Once"
        val address = CartFragment.selectedAddressEntity
        val tmpCartId = cartId

        orderSuccessViewModel.placeOrder(tmpCartId,
            PaymentFragment.paymentMode,address!!.addressId,"Pending","Pending",deliveryFrequency)
        
        orderSuccessViewModel.orderedId.observe(viewLifecycleOwner){
            orderSuccessViewModel.getOrderAndCorrespondingCart(it.toInt())
            val selectedTimeSlot = arguments?.getInt("timeSlotInt")
            val dayOfWeek = arguments?.getInt("dayOfWeek")
            val dayOfMonth = arguments?.getInt("dayOfMonth")
            if(deliveryFrequency!="Once"){
                selectedTimeSlot?.let {selectedSlot ->
                    orderSuccessViewModel.addOrderToTimeSlot(TimeSlot(it.toInt(),selectedSlot))
                }
                if(deliveryFrequency=="Daily"){
                    orderSuccessViewModel.addDailySubscription(DailySubscription(it.toInt()))
                }
                else if(deliveryFrequency=="Weekly Once"){
                    dayOfWeek?.let {weekId->
                        orderSuccessViewModel.addWeeklySubscription(WeeklyOnce(it.toInt(),weekId))
                    }
                }
                else if(deliveryFrequency=="Monthly Once"){
                    dayOfMonth?.let {monthNumber->
                        orderSuccessViewModel.addMonthlySubscription(MonthlyOnce(it.toInt(),monthNumber))
                    }
                }

            }
        }
        orderSuccessViewModel.orderWithCart.observe(viewLifecycleOwner){
            if(it.values.isNotEmpty() && it.keys.isNotEmpty()) {
                var selectedOrder:OrderDetails? = null
                for (i in it) {
                    selectedOrder = i.key
                    OrderListFragment.correspondingCartList = i.value
                }
                selectedOrder?.let { doFragmentTransaction(it) }
            }
        }

        orderSuccessViewModel.updateAndAssignNewCart(cartId, userId.toInt())
        orderSuccessViewModel.newCart.observe(viewLifecycleOwner){
            cartId = it.cartId
        }

        return view
    }

    private fun doFragmentTransaction(selectedOrder:OrderDetails) {
        val orderDetailFrag = OrderDetailFragment()

        view?.findViewById<LinearLayout>(R.id.progressBarInOrderSummary)?.visibility = View.GONE
        view?.findViewById<LinearLayout>(R.id.tickMark)?.animate()
            ?.alpha(1f)
            ?.setDuration(100)
            ?.withEndAction {
                view?.findViewById<LinearLayout>(R.id.tickMark)?.visibility =View.VISIBLE
            }
            ?.start()
        orderDetailFrag.arguments = Bundle().apply {
            putBoolean("hideToolBar",true)
            putBoolean("hideCancelOrderButton",true)
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
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            .replace(R.id.orderSummaryFragment,orderDetailFrag)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        InitialFragment.hideBottomNav.value = true
        InitialFragment.hideSearchBar.value = true
    }

    override fun onPause() {
        super.onPause()
    }

    private fun restartApp() {
        PaymentFragment.paymentMode =""
        val intent = Intent(context,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        InitialFragment.hideBottomNav.value = false
        InitialFragment.hideSearchBar.value = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}