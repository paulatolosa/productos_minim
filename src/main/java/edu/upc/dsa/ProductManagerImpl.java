package edu.upc.dsa;

import edu.upc.dsa.exceptions.OrderNoExisteixException;
import edu.upc.dsa.exceptions.ProductNoExisteixException;
import edu.upc.dsa.exceptions.UserNoExisteixException;
import edu.upc.dsa.models.Item;
import edu.upc.dsa.models.Order;
import edu.upc.dsa.models.Product;
import edu.upc.dsa.models.User;

import java.util.List;
import org.apache.log4j.Logger;
import java.util.*;

public class ProductManagerImpl implements ProductManager {

    private static ProductManagerImpl instance;
    private static final Logger logger = Logger.getLogger(ProductManagerImpl.class);

    List<Product> products;
    List<User> users;
    Queue<Order> pendingOrders;
    List<Order> servedOrders ;

    private ProductManagerImpl() {
        this.products = new ArrayList<>();
        this.users = new ArrayList<>();
        this.pendingOrders = new LinkedList<>();
        this.servedOrders = new ArrayList<>();
    }

    public static ProductManagerImpl getInstance() {
        if (instance == null) instance = new ProductManagerImpl();
        return instance;
    }

    @Override
    public void addProduct(String id, String name, double price, int sales) {
        products.add(new Product(id,name,price,sales));
    }

    @Override
    public List<Product> productsByPrice() {
        logger.info("productsByPrice() called");

        List<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparingDouble(Product::getPrice));

        logger.info("productsByPrice() finished. Returning " + sorted.size() + " products.");
        return sorted;
    }

    @Override
    public void addOrder(Order order) {
        logger.info("addOrder() called with order id=" + order.getId());
        try{
            if(order==null){
                logger.error("No existeix el order");
                throw new OrderNoExisteixException("No existeix order");
            }
            else{
                if(order.getUser()==null){
                    logger.error("No existeix el user");
                    throw new UserNoExisteixException("No existeix user amb ID="+order.getUser().getId());
                }
                else{
                    User user=findUserById(order.getUser().getId());
                    pendingOrders.add(order);
                    logger.info("Order amb ID="+order.getId()+"creat i pendent de servir");
                }
            }
        }
        catch(OrderNoExisteixException|UserNoExisteixException ex){
            logger.error("Excepció:"+ex.getMessage());
        }
    }

    @Override
    public void serveOrder() {
        logger.info("serveOrder() called");
        try{
            if(pendingOrders.isEmpty()){
                logger.error("No hi han orders pendents a servir.");
            }
            else{
                Order order = pendingOrders.poll();
                servedOrders.add(order);

                for (Item item : order.getItems()) {
                    Product product = findProductById(item.getId());
                    if (product != null) {
                        product.setSales(product.getSales() + item.getQuantity());
                        logger.info("Actulitzades les ventes del producte " + product.getName() + " -> total de vendes ara: " + product.getSales());
                    } else {
                        logger.error("No s'ha trobat producte amb ID=" + item.getId() + " a la llista d'orders pendents");
                        throw new ProductNoExisteixException("No trobat el producte amb ID=" + item.getId());
                    }
                }
                logger.info("Ordre amb ID="+order.getId()+" servit correctament");
            }

        }
        catch(ProductNoExisteixException ex){
            logger.error("Excepció: No s'ha trobat producte");
        }
    }

    @Override
    public List<Order> ordersServedToUser(String userId) {
        logger.info("ordersServedToUser() called for userId=" + userId);
        try{
            if(findUserById(userId) == null){
                logger.error("No existeix el user");
                throw new UserNoExisteixException("No existeix order amb ID="+userId);
            }
            else{
                List<Order> result = new ArrayList<>();
                for (Order o : servedOrders) {
                    if (o.getUser().getId().equals(userId)) {
                        result.add(o);
                    }
                }
                logger.info("Returning " + result.size() + " served orders for user " + userId);
                return result;
            }
        }
        catch(UserNoExisteixException ex){
            logger.error("Excepció: No existeix user");
            return null;
        }
    }

    @Override
    public List<Product> productsBySales() {
        logger.info("productsBySales() called");

        List<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparingInt(Product::getSales).reversed());

        logger.info("productsBySales() finished. Returning " + sorted.size() + " products.");
        return sorted;
    }


    // necessari per implementar els metodes
    public User findUserById(String userId){
        for (User u : users) {
            if (u.getId().equals(userId)) return u;
        }
        return null;
    }
    public Product findProductById(String productId){
        for (Product p : products) {
            if (p.getId().equals(productId)) return p;
        }
        return null;
    }

    // necessari per la rest api
    public int numOrders(){
        return pendingOrders.size();
    }
    public void addUser(String userId){
        User user = new User(userId);
        users.add(user);
    }
    public List<User> getUsers(){
        return users;
    }
}


