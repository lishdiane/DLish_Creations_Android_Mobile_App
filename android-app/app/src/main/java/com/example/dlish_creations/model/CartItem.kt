package com.example.dlish_creations.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Represents an individual item in the shopping cart.
 *
 * Stores a Product and the quantity selected by the user.
 */
class CartItem (
    val product: Product,
    quantity: Int
) {
    var quantity by mutableIntStateOf(quantity)
}