package com.finki.explorandija.controller;

import com.finki.explorandija.dto.AuthRequest;
import com.finki.explorandija.dto.AuthResponse;
import com.finki.explorandija.dto.RegistrationRequest;
import com.finki.explorandija.model.User;
import com.finki.explorandija.service.UserService;
import com.finki.explorandija.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

// import java.util.Optional; // No longer needed directly here for Optional.isPresent()

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil; // Injected

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        if (registrationRequest.username() == null || registrationRequest.username().trim().isEmpty() ||
            registrationRequest.password() == null || registrationRequest.password().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password cannot be empty");
        }
        if (userService.getUserByUsername(registrationRequest.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        User newUser = new User();
        newUser.setUsername(registrationRequest.username());
        newUser.setPassword(registrationRequest.password());
        User savedUser = userService.createUser(newUser);
        
        // Optionally, log the user in immediately by generating and returning a JWT
        final UserDetails userDetailsForToken = userService.loadUserByUsername(savedUser.getUsername());
        final String jwt = jwtUtil.generateToken(userDetailsForToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(jwt, savedUser.getUsername(), savedUser.getId()));
        // Or, simply return a success message:
        // return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. Please login.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.getUserByUsername(userDetails.getUsername())
                            .orElseThrow(() -> new BadCredentialsException("User details not found after authentication"));

            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(jwt, user.getUsername(), user.getId()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            // Log the exception for server-side review
            System.err.println("Login error: " + e.getMessage()); 
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed due to an internal error. Please try again later.");
        }
    }
} 