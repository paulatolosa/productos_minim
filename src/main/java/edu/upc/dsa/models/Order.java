package edu.upc.dsa.models;

import java.util.List;

public class Order {

    String id;
    User user;
    List<Item>  items;

    public Order(){}

    public Order(String id, User user, List<Item> items) {
        this.id = id;
        this.user = user;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }



}
