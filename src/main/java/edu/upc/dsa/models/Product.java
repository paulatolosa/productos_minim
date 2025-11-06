package edu.upc.dsa.models;

public class Product {

    String id;
    String name;
    double price;
    int sales;

    public Product(){}

    public Product(String id, String name, double price, int sales) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sales = sales;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSales() {
        return this.sales;
    }

    public void setSales(int quantity) {
        this.sales +=quantity;
    }


}