package com.example.milkit.presentation.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.milkit.data.repo.CartRepository
import com.example.milkit.data.room.CartDao
import com.example.milkit.data.room.MyRoomDatabase
import com.example.milkit.presentation.cart.CartModel

class CheckoutViewModel:ViewModel() {


    var db: MyRoomDatabase =  MyRoomDatabase.getInstance()
    var cartDao: CartDao =db.cartDao()
    var cartRepo: CartRepository = CartRepository(cartDao)

    val cartItems: LiveData<List<CartModel>> = cartRepo.allCartItems


    init {
        getAllCartItems()
    }


    fun getAllCartItems() {
        cartRepo.getAllCartsItems()
    }
}