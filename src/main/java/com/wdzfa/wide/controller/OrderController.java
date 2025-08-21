package com.wdzfa.wide.controller;

import com.wdzfa.wide.dto.CartItemRequest;
import com.wdzfa.wide.dto.ResponseData;
import com.wdzfa.wide.model.Cart;
import com.wdzfa.wide.model.Order;
import com.wdzfa.wide.service.OrderService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<ResponseData<Cart>> addToCart(@RequestBody CartItemRequest request, Errors errors) {

        ResponseData<Cart> responseData = new ResponseData<>();
        if (errors.hasErrors()) {
            for (Object obj : errors.getAllErrors()) {
                responseData.setStatus(false);
                responseData.setPayload(null);
                responseData.getMessage().add(obj.toString());
                return ResponseEntity.badRequest().body(responseData);
            }
        }
        return orderService.addToCart(request);
    }

    @GetMapping("/{id}")
    public Cart findById(@PathVariable("id") Long id){
        return orderService.findOne(id);
    }

    @GetMapping("/all-cart")
    public Iterable<Cart> findAll(){
        return orderService.cartList();
    }

    @GetMapping("/all-cart/{size}/{page}/{sort}")
    public Iterable<Cart> findWithPagedAndSorted(@PathVariable("page") int page,
                                                 @PathVariable("size") int size,
                                                 @PathVariable("sort") String sort){
        return orderService.findWithPagedAndSorted(page, size, sort);
    }

    @PostMapping("/place-order/{id}/{checkedOut}")
    public ResponseEntity<ResponseData<Order>> placeOrder(@PathVariable("id") Long id,
                                                          @PathVariable("checkedOut") String checkedOut) {

        return orderService.placeOrder(id, checkedOut);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeCart(@PathVariable("id") Long id){
        return orderService.remove(id);
    }

    @PutMapping("/add/{id}/{stock}")
    public ResponseEntity<String> addStock(@PathVariable("id") Long id,
                                           @PathVariable("stock") int stock){
        return orderService.addStock(id,stock);
    }

    @GetMapping("/all-cart/{size}/{page}/{name}/{sort}")
    public Iterable<Cart> findAllCartUser(@PathVariable("page") int page,
                                          @PathVariable("size") int size,
                                          @PathVariable("name") String name,
                                          @PathVariable("sort") String sort){
        return orderService.findAllCartEveryUser(page,size,name,sort);
    }

}
