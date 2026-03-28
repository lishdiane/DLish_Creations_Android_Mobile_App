package com.example.dlish_creations.model

import androidx.compose.runtime.mutableStateListOf

/**
 * Represents a shopping cart in the application.
 *
 * Stores a list of cart items and provides functionality to:
 * - Add products to the cart
 * - Update item quantities
 * - Calculate the total cost
 */
class Cart {

    val cartItems = mutableStateListOf<CartItem>()

    fun addProduct(product: Product) {
        val existingItem = cartItems.find { it.product.name == product.name }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(product,1))
        }
    }

    fun getTotal(): Double {
        return cartItems.sumOf { it.product.price * it.quantity}
    }
}