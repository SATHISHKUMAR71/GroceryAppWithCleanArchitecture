package com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.forgotpassword

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.shoppinggroceryapp.views.sharedviews.profileviews.EditProfileViewModel
import com.example.shoppinggroceryapp.helpers.inputvalidators.interfaces.InputChecker
import com.example.shoppinggroceryapp.helpers.inputvalidators.TextLayoutInputChecker
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class ForgotPasswordFragment : Fragment() {


    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var textInputChecker: InputChecker
    private lateinit var emailOrPhoneLayout:TextInputLayout
    private lateinit var newPasswordLayout:TextInputLayout
    private lateinit var emailOrPhoneEditText:TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var confirmPasswordEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textInputChecker = TextLayoutInputChecker()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        emailOrPhoneLayout = view.findViewById(R.id.textInputLayoutMailOrPhone)
        newPasswordLayout = view.findViewById(R.id.textInputLayoutNewPassword)
        emailOrPhoneEditText = view.findViewById(R.id.forgotEmailInput)
        passwordEditText = view.findViewById(R.id.inputNewPassword)
        confirmPasswordLayout = view.findViewById(R.id.textInputLayoutNewConfirmPassword)
        confirmPasswordEditText = view.findViewById(R.id.inputNewConfirmPassword)
        view.findViewById<MaterialToolbar>(R.id.appbarLayoutForgotPass).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        emailOrPhoneEditText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                emailOrPhoneLayout.error = null
            }
        }

        passwordEditText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                newPasswordLayout.error = null
            }
        }
        confirmPasswordEditText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                confirmPasswordLayout.error = null
            }
        }

        setUpViewModel()
        val userData  = emailOrPhoneEditText.text

        view.findViewById<MaterialButton>(R.id.materialButtonUpdatePassword).setOnClickListener {
            editProfileViewModel.getUser(userData.toString())
        }

        editProfileViewModel.userEntity.observe(viewLifecycleOwner){
            passwordEditText.clearFocus()
            emailOrPhoneEditText.clearFocus()
            confirmPasswordEditText.clearFocus()
            emailOrPhoneLayout.error = textInputChecker.emptyCheck(emailOrPhoneEditText)
            newPasswordLayout.error = textInputChecker.lengthAndEmptyCheck("Password",passwordEditText,6)
            confirmPasswordLayout.error = textInputChecker.emptyCheck(confirmPasswordEditText)

            if(emailOrPhoneLayout.error == null && newPasswordLayout.error == null && confirmPasswordLayout.error == null){
                if(confirmPasswordEditText.text.toString()!=passwordEditText.text.toString()){
                    confirmPasswordLayout.error = "Password Should be same"
                }
                else if(it != null){
                    if(passwordEditText.text.toString().isNotEmpty()){
                        editProfileViewModel.savePassword(it.copy(userPassword = passwordEditText.text.toString()))
                        showSnackBar("Password Updated Successfully",view)
                        parentFragmentManager.popBackStack()
                    }
                    else{
                        showSnackBar("Password should not be empty",view)
                    }
                }
                else{
                    emailOrPhoneLayout.error = "User Not Found !"
                    showSnackBar("Email or Mobile Didn't Exist",view)
                }
            }
        }
        return view
    }

    private fun setUpViewModel() {
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        editProfileViewModel = ViewModelProvider(this,GroceryAppSharedVMFactory(retailerDao, userDao))[EditProfileViewModel::class.java]
    }

    private fun showSnackBar(text:String, view: View){
        Snackbar.make(view,text,Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(Color.argb(255,230,20,20))
                .show()
        }
    }
}