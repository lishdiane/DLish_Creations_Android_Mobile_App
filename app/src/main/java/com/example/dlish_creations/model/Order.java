package com.example.dlish_creations.model;

public class Order {
    private Cart cart;
    private Customer customer;

    public Order(Cart cart, Customer customer) {
        this.cart = cart;
        this.customer = customer;
    }

    // Getters
    public Cart getCart() {
        return cart;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotal() {
        return cart.getTotal();
    }

    public void displayOrder() {
        System.out.println("Customer: " + customer.getName());
        System.out.println("Phone Number: " + customer.getPhoneNumber());

    }

}
