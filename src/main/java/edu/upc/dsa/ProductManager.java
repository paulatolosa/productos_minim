package edu.upc.dsa;

import edu.upc.dsa.models.Product;
import edu.upc.dsa.models.User;
import edu.upc.dsa.models.Order;

import java.util.List;

public interface ProductManager {

    public List<Product> productsByPrice();
    public void addOrder(Order order);
    public void serveOrder();
    public List<Order> ordersServedToUser(String userId);
    public List<Product> productsBySales();

    // necessari per implementar els metodes
    public void addProduct(String id, String name, double price, int sales);
    public User findUserById(String userId);
    public Product findProductById(String productId);

    // necessari per la REST API
    public int numOrders();
    public void addUser(String userId);
    public List<User> getUsers();


}
