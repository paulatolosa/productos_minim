package edu.upc.dsa.services;

import edu.upc.dsa.ProductManagerImpl;
import edu.upc.dsa.models.*;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Api(value = "/products", description = "Endpoint to ProductManager Service")
@Path("/products")
public class ProductsService {


    private ProductManagerImpl ps;

    public ProductsService() {
        this.ps = ProductManagerImpl.getInstance();

        // Inicialitzem dades de prova només si està buit
        if (ps.numOrders()==0) {


            // Afegim productes
            ps.addProduct("P1", "Cafè", 1.5, 0);
            ps.addProduct("P2", "Entrepà", 3.0, 0);
            ps.addProduct("P3", "Croissant", 2.0, 0);

            // Afegim usuaris
            ps.addUser("U1");
            ps.addUser("U2");
        }
    }


    @POST
    @ApiOperation(value = "Add a new product", notes = "Creates a new product with ID, name and price")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Product created", response = Product.class),
            @ApiResponse(code = 400, message = "Validation Error")
    })
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(Product product) {
        if (product.getId() == null || product.getName() == null)
            return Response.status(400).entity("Missing product ID or name").build();

        ps.addProduct(product.getId(), product.getName(), product.getPrice(), 0);
        return Response.status(201).entity(product).build();
    }


    @POST
    @ApiOperation(value = "Add a new order", notes = "Creates an order with user and items")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Order created", response = Order.class),
            @ApiResponse(code = 400, message = "Validation Error")
    })
    @Path("/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrder(Order order) {
        if (order == null || order.getUser() == null || order.getItems() == null)
            return Response.status(400).entity("Missing order, user or items").build();

        ps.addOrder(order);
        return Response.status(201).entity(order).build();
    }


    @PUT
    @ApiOperation(value = "Serve a pending order", notes = "Serves the first pending order in the queue")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order served"),
            @ApiResponse(code = 404, message = "No pending orders")
    })
    @Path("/serve")
    @Produces(MediaType.TEXT_PLAIN)
    public Response serveOrder() {
        if (ps.numOrders()==0)
            return Response.status(404).entity("No pending orders to serve").build();

        ps.serveOrder();
        return Response.status(200).entity("Order served successfully").build();
    }


    @GET
    @ApiOperation(value = "Get all served orders for a user", notes = "Returns all served orders for a specific user ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Order.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/ordersServed/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersServedToUser(@PathParam("userId") String userId) {
        if (ps.findUserById(userId) == null)
            return Response.status(404).entity("User not found").build();

        List<Order> orders = ps.ordersServedToUser(userId);
        GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(orders) {};
        return Response.status(200).entity(entity).build();
    }


    @GET
    @ApiOperation(value = "Get all products sorted by price", notes = "Returns products ordered by ascending price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer = "List")
    })
    @Path("/byPrice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByPrice() {
        List<Product> products = ps.productsByPrice();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }


    @GET
    @ApiOperation(value = "Get all products sorted by sales", notes = "Returns products ordered by descending sales count")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer = "List")
    })
    @Path("/bySales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsBySales() {
        List<Product> products = ps.productsBySales();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }


    @GET
    @ApiOperation(value = "Get all users", notes = "Returns all users registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = User.class, responseContainer = "List")
    })
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<User> users = ps.getUsers();
        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
        return Response.status(200).entity(entity).build();
    }
}

