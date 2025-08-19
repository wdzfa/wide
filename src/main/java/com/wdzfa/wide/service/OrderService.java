package com.wdzfa.wide.service;

import com.wdzfa.wide.dto.CartItemRequest;
import com.wdzfa.wide.dto.ResponseData;
import com.wdzfa.wide.model.Cart;
import com.wdzfa.wide.model.Order;
import com.wdzfa.wide.model.Product;
import com.wdzfa.wide.model.User;
import com.wdzfa.wide.repository.CartRepository;
import com.wdzfa.wide.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public ResponseEntity<ResponseData<Cart>> addToCart(CartItemRequest request) {
        ResponseData<Cart> responseData = new ResponseData<>();

        Optional<User> userOptional = userService.findByName(request.getUserName());
        if (userOptional.isEmpty()) {
            responseData.setStatus(false);
            responseData.getMessage().add("User not found: " + request.getUserName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
        User user = userOptional.get();

        Optional<Product> productOptional = productService.findProductByType(request.getProductType());
        if (productOptional.isEmpty()) {
            responseData.setStatus(false);
            responseData.getMessage().add("Product not found for type: " + request.getProductType());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
        Product product = productOptional.get();

        if (product.getStock() < request.getQuantity()) {
            responseData.setStatus(false);
            responseData.getMessage().add("Insufficient stock for product: " + product.getName() + ". Available stock: " + product.getStock());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        // Calculate total price
        double totalCalculatedPrice = product.getPrice() * request.getQuantity();

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(request.getQuantity());
        cart.setTotalPrice(totalCalculatedPrice);
        cart.setOrderDate(LocalDateTime.now());
        cart.setPlaceOrder("false");
        Cart savedOrder = cartRepository.save(cart);

        responseData.setStatus(true);
        responseData.getMessage().add("Order placed successfully!");
        responseData.setPayload(savedOrder);
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<ResponseData<Order>> placeOrder(Long id, String checkOut){

        ResponseData<Order> responseData = new ResponseData<>();

        Cart cart  = cartRepository.findByCartId(id);
        if (checkOut == null || checkOut.equalsIgnoreCase("false")) {
            responseData.setStatus(false);
            responseData.getMessage().add("Order not placed because: " + checkOut + " mean you don't want to check out product");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        Product deductProduct = productService.findOne(cart.getProduct().getId());
        deductProduct.setStock(deductProduct.getStock() - cart.getQuantity());
        productService.create(deductProduct);

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setProduct(cart.getProduct());
        order.setQuantity(cart.getQuantity());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        responseData.setStatus(true);
        responseData.getMessage().add("Order Place Successfully");
        responseData.setPayload(savedOrder);
        remove(cart.getId());
        return ResponseEntity.ok(responseData);
    }

    public void remove(Long id){
        cartRepository.deleteById(id);
    }}
