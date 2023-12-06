package com.example.milkit.presentation.cart

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.TypeConverter
import com.example.milkit.data.repo.CartRepository
import com.example.milkit.data.room.CartDao
import com.example.milkit.data.room.MyRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel:ViewModel() {


    var db:MyRoomDatabase =  MyRoomDatabase.getInstance()
    var cartDao:CartDao =db.cartDao()
    var cartRepo:CartRepository = CartRepository(cartDao)

    val cartItems: LiveData<List<CartModel>> = cartRepo.allCartItems




    init {
        getAllCartItems()
    }


    fun getAllCartItems() {
        cartRepo.getAllCartsItems()
    }

    fun addToCart(cartModel: CartModel) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.addNewItem(cartModel)
        }
    }

    fun editCartItem(cartModel: CartModel) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.updateCartItem(cartModel)
        }
    }
}

