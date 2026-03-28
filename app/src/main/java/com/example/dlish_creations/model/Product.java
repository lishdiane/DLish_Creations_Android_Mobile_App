package com.example.dlish_creations.model;

public class Product {
    // Class for Products
    private final String id;
    private String name;
    private String material;
    private Size size;
    private double price;

    public Product(String id, String name, String material, Size size, double price) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.size = size;
        this.price = price;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMaterial() {
        return material;
    }

    public Size getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public static class Size {
        // Class for product size
        private double heightIn;
        private double widthIn;
        private double depthIn;

        public Size(double heightIn, double widthIn, double depthIn) {
            // Constructor for size
            this.heightIn = heightIn;
            this.widthIn = widthIn;
            this.depthIn = depthIn;
        }

        public double getHeightIn() {
            return heightIn;
        }

        public double getWidthIn() {
            return widthIn;
        }

        public double getDepthIn() {
            return depthIn;
        }

        @Override
        public String toString() {
            return heightIn + " x " + widthIn + " x " + depthIn + " in";
        }
    }

    public void displayInfo() {
        System.out.println("Product: " + name);
        System.out.println("Material: " + material);
        System.out.println("Size: " + size);
        System.out.printf("Price: $%.2f%n", price);
        System.out.println();
    }

}
