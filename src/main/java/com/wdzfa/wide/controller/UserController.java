package com.wdzfa.wide.controller;

import com.wdzfa.wide.dto.ResponseData;
import com.wdzfa.wide.dto.UserRequest;
import com.wdzfa.wide.model.User;
import com.wdzfa.wide.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<ResponseData<User>> register(@RequestBody UserRequest request, Errors errors){

        ResponseData<User> responseData = new ResponseData<>();
        if (errors.hasErrors()){
            for (Object object: errors.getAllErrors()){
                responseData.setStatus(false);
                responseData.getMessage().add("User already registered");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
        }

        User user = modelMapper.map(request,User.class);
        responseData.setStatus(true);
        responseData.setPayload(userService.register(user));
        return ResponseEntity.ok(responseData);
    }
}
