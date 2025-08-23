package com.wdzfa.wide.service;

import com.wdzfa.wide.dto.ResponseData;
import com.wdzfa.wide.dto.UserRequest;
import com.wdzfa.wide.model.User;
import com.wdzfa.wide.repository.UserRepository;
import com.wdzfa.wide.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseData<User> register(UserRequest request) {
        ResponseData<User> response = new ResponseData<>();
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setStatus(false);
            response.getMessage().add("Email sudah terdaftar");
            return response;
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        response.setStatus(true);
        response.getMessage().add("Registrasi berhasil");
        return response;
    }
    public Optional<User> findByName(String name) {
        return userRepository.findUserByName(name);
    }

    public ResponseData<String> login(UserRequest request) {
        ResponseData<String> response = new ResponseData<>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception ex) {
            response.setStatus(false);
            response.getMessage().add("Email atau password salah");
            return response;
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            response.setStatus(false);
            response.getMessage().add("User tidak ditemukan setelah autentikasi");
            return response;
        }

        String accessToken = jwtService.generateToken(user.getEmail());
        user.setRefreshToken(accessToken);
        userRepository.save(user);

        response.setStatus(true);
        response.getMessage().add("Login berhasil");
        response.setPayload(accessToken);
        return response;
    }
}
