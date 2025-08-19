package com.wdzfa.wide.controller;

import com.wdzfa.wide.dto.CartItemRequest;
import com.wdzfa.wide.dto.ResponseData;
import com.wdzfa.wide.model.Cart;
import com.wdzfa.wide.model.Order;
import com.wdzfa.wide.service.OrderService;
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

    @PostMapping("/place-order/{id}/{checkedOut}")
    public ResponseEntity<ResponseData<Order>> placeOrder(@PathVariable("id") Long id,
                                                          @PathVariable("checkedOut") String checkedOut) {

        return orderService.placeOrder(id, checkedOut);
    }
}
