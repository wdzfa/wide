package com.wdzfa.wide.service;

import com.wdzfa.wide.dto.CartItemRequest;
import com.wdzfa.wide.dto.ResponseData;
import com.wdzfa.wide.model.Cart;
import com.wdzfa.wide.model.Order;
import com.wdzfa.wide.model.Product;
import com.wdzfa.wide.model.User;
import com.wdzfa.wide.repository.CartPagingAndSortingRepository;
import com.wdzfa.wide.repository.CartRepository;
import com.wdzfa.wide.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    CartPagingAndSortingRepository cartPagingAndSortingRepository;

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

        Optional<Product> productOptional = productService.findProductByName(request.getProductName());
        if (productOptional.isEmpty()) {
            responseData.setStatus(false);
            responseData.getMessage().add("Product not found: " + request.getProductName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
        Product product = productOptional.get();

        if (product.getStock() < request.getQuantity()) {
            responseData.setStatus(false);
            responseData.getMessage().add("Insufficient stock for product: " + product.getName() + ". Available stock: " + product.getStock());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Iterable<Cart> allCarts = this.cartList();
        int totalQuantityInCart = 0;
        for (Cart cart : allCarts) {
            if (cart.getProduct().getId().equals(product.getId())) {
                totalQuantityInCart += cart.getQuantity();
            }
        }

        if (totalQuantityInCart + request.getQuantity() > product.getStock()) {
            responseData.setStatus(false);
            responseData.getMessage().add("Not enough stock available");
            return ResponseEntity.badRequest().body(responseData);
        }

        double totalCalculatedPrice = product.getPrice() * request.getQuantity();

        Cart cart;
        cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(request.getQuantity());
        cart.setTotalPrice(totalCalculatedPrice);
        cart.setOrderDate(LocalDateTime.now());
        cart.setPlaceOrder("false");

        cartRepository.save(cart);

        responseData.setStatus(true);
        responseData.getMessage().add("Cart updated successfully");
        responseData.setPayload(cart);
        return ResponseEntity.ok(responseData);

    }

    public Cart findOne(Long id) {
        Optional<Cart> cart = cartRepository.findById(id);
        return cart.get();
    }

    public Iterable<Cart> cartList() {
        return cartRepository.findAll();
    }

    public Iterable<Cart> findWithPagedAndSorted(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        if (sort.equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(page, size, Sort.by("id").descending());
        }
        return cartPagingAndSortingRepository.findAll(pageable);
    }

    public ResponseEntity<ResponseData<Order>> placeOrder(Long id, String checkOut) {

        ResponseData<Order> responseData = new ResponseData<>();

        Cart cart = cartRepository.findByCartId(id);
        if (checkOut == null || checkOut.equalsIgnoreCase("false")) {
            responseData.setStatus(false);
            responseData.getMessage().add("Order not placed because: " + checkOut + " mean you don't want to check out product");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        Product deductProduct = productService.findOne(cart.getProduct().getId());

        if (deductProduct.getStock() < cart.getQuantity()) {
            responseData.setStatus(false);
            responseData.getMessage().add("Insufficient stock for product: " + deductProduct.getName() + ". Available stock: " + deductProduct.getStock());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

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
        responseData.getMessage().add("Order Placed Successfully");
        responseData.setPayload(savedOrder);
        remove(cart.getId());
        return ResponseEntity.ok(responseData);
    }

    public ResponseEntity<String> remove(Long id) {
        cartRepository.deleteById(id);
        return new ResponseEntity<>("Cart Removed", HttpStatus.OK);
    }

    public ResponseEntity<String> addStock(Long id, int stock){
        Cart cart = cartRepository.findByCartId(id);
        cart.setQuantity(cart.getQuantity() + stock);
        cartRepository.save(cart);

        return new ResponseEntity<>("New Stock Placed", HttpStatus.OK);
    }

    public Page<Cart> findAllCartEveryUser(int page, int size, String name, String sort){

        Pageable pageable = PageRequest.of(page,size,Sort.by("id"));
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(page,size,Sort.by("id").descending());
        }

        User user = userService.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findAllCartUser(user,pageable);
    }
}
