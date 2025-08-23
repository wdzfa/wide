package com.wdzfa.wide.controller;

import com.wdzfa.wide.dto.AuthRequest;
import com.wdzfa.wide.dto.ResponseData;
import com.wdzfa.wide.dto.UserRequest;
import com.wdzfa.wide.model.User;
import com.wdzfa.wide.service.UserService;
import com.wdzfa.wide.service.jwt.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ResponseData<User>> register(@RequestBody UserRequest request) {
        ResponseData<User> response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData<String>> login(@RequestBody UserRequest request) {
        ResponseData<String> response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken (@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                        request.getPassword()));
        if (authentication.isAuthenticated()){
            return jwtService.generateToken(request.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
