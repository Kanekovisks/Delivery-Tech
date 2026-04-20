package com.deliverytech.delivery_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.LoginRequestDTO;
import com.deliverytech.delivery_api.dto.responses.LoginResponseDTO;
import com.deliverytech.delivery_api.enums.UserRole;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.UserRepository;
import com.deliverytech.delivery_api.security.JwtUtil;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints de controle de autorização.")
public class AuthenticationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationController (UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado.");
        }

        User user = new User();
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(
            request.getRole() != null ? request.getRole() : UserRole.CLIENT
        );

        user.setActive(true);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO login) {

        User user = userRepository.findByEmail(login.getEmail())
                .orElse(null);

        if (user == null ||
            !passwordEncoder.matches(login.getPassword(), user.getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas.");
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication auth) {

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return ResponseEntity.ok(user);
    }
}
