package com.example.shoppinggroceryapp.helpers.fragmenttransaction

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.shoppinggroceryapp.R

class FragmentTransaction {
    companion object{
        fun navigateWithBackstack(fragmentManager:FragmentManager,fragment:Fragment,backstack:String){
            println("87678 TAG VALUE: $backstack")
            val frag = fragmentManager.findFragmentByTag(backstack)
            frag?.let {
                fragmentManager.popBackStack(
                    backstack,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
             fragmentManager.beginTransaction()
                 .setCustomAnimations(
                     R.anim.fade_in,
                     R.anim.fade_out,
                     R.anim.fade_in,
                     R.anim.fade_out
                 )
                 .setReorderingAllowed(true)
                 .replace(R.id.fragmentMainLayout, fragment, backstack)
                 .addToBackStack(backstack)
                 .commit()

        }
    }
}