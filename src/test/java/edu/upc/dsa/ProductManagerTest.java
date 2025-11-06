package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductManagerTest {

    ProductManagerImpl manager;

    @Before
    public void setUp() {
        manager = ProductManagerImpl.getInstance();
        // Netejem les estructures abans de començar
        manager.products.clear();
        manager.users.clear();
        manager.pendingOrders.clear();
        manager.servedOrders.clear();

        // Afegim productes
        manager.addProduct("P1", "Cafè", 1.5, 0);
        manager.addProduct("P2", "Entrepà", 3.0, 0);
        manager.addProduct("P3", "Croissant", 2.0, 0);

        // Afegim usuaris
        manager.users.add(new User("U1"));
        manager.users.add(new User("U2"));
    }

    @After
    public void tearDown() {
        manager.products.clear();
        manager.users.clear();
        manager.pendingOrders.clear();
        manager.servedOrders.clear();
    }

    @Test
    public void testProductsByPrice() {
        List<Product> sorted = manager.productsByPrice();
        Assert.assertEquals("P1", sorted.get(0).getId()); // Cafè (1.5€)
        Assert.assertEquals("P3", sorted.get(1).getId()); // Croissant (2€)
        Assert.assertEquals("P2", sorted.get(2).getId()); // Entrepà (3€)
    }

    @Test
    public void testAddOrder() {
        User user = manager.findUserById("U1");
        List<Item> items = new ArrayList<>();
        items.add(new Item("P1", "Cafè", 2));
        items.add(new Item("P2", "Entrepà", 1));

        Order order = new Order("O1", user, items);
        manager.addOrder(order);

        Assert.assertEquals(1, manager.pendingOrders.size());
        Assert.assertEquals("O1", manager.pendingOrders.peek().getId());
    }

    @Test
    public void testServeOrderUpdatesSales() {
        User user = manager.findUserById("U1");
        List<Item> items = new ArrayList<>();
        items.add(new Item("P1", "Cafè", 2));
        items.add(new Item("P3", "Croissant", 1));

        Order order = new Order("O2", user, items);
        manager.addOrder(order);

        manager.serveOrder();

        Product p1 = manager.findProductById("P1");
        Product p3 = manager.findProductById("P3");

        Assert.assertEquals(2, p1.getSales());
        Assert.assertEquals(1, p3.getSales());
        Assert.assertEquals(1, manager.servedOrders.size());
    }

    @Test
    public void testOrdersServedToUser() {
        User user = manager.findUserById("U1");

        List<Item> items1 = new ArrayList<>();
        items1.add(new Item("P1", "Cafè", 1));
        Order order1 = new Order("O3", user, items1);

        List<Item> items2 = new ArrayList<>();
        items2.add(new Item("P2", "Entrepà", 1));
        Order order2 = new Order("O4", user, items2);

        manager.addOrder(order1);
        manager.addOrder(order2);
        manager.serveOrder();
        manager.serveOrder();

        List<Order> served = manager.ordersServedToUser("U1");
        Assert.assertEquals(2, served.size());
        Assert.assertEquals("O3", served.get(0).getId());
        Assert.assertEquals("O4", served.get(1).getId());
    }

    @Test
    public void testProductsBySales() {
        User user = manager.findUserById("U1");

        List<Item> items1 = new ArrayList<>();
        items1.add(new Item("P1", "Cafè", 3));
        Order o1 = new Order("O5", user, items1);
        manager.addOrder(o1);
        manager.serveOrder();

        List<Item> items2 = new ArrayList<>();
        items2.add(new Item("P3", "Croissant", 1));
        Order o2 = new Order("O6", user, items2);
        manager.addOrder(o2);
        manager.serveOrder();

        List<Product> sorted = manager.productsBySales();
        Assert.assertEquals("P1", sorted.get(0).getId()); // més vendes
        Assert.assertEquals("P3", sorted.get(1).getId());
    }

    @Test
    public void testFindUserAndProductById() {
        Product p = manager.findProductById("P1");
        User u = manager.findUserById("U1");

        Assert.assertNotNull(p);
        Assert.assertEquals("Cafè", p.getName());
        Assert.assertNotNull(u);
        Assert.assertEquals("U1", u.getId());
    }
}
