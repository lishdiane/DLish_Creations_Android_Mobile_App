package com.example.dlish_creations.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CartItem (
    val product: Product,
    quantity: Int
) {
    var quantity by mutableIntStateOf(quantity)
}