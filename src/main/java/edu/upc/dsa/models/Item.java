package edu.upc.dsa.models;

public class Item {

    String id;
    String name;
    int quantity;

    public Item(){}

    public Item(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
