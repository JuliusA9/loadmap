package com.loadmap.planner.controller;

import com.loadmap.planner.model.User;
import com.loadmap.planner.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final SecurityContextRepository contextRepo = new HttpSessionSecurityContextRepository();

    public AuthController(UserRepository users, PasswordEncoder encoder, AuthenticationManager authManager) {
        this.users = users;
        this.encoder = encoder;
        this.authManager = authManager;
    }

    // The JSON the page sends: {"email": "...", "password": "..."}
    public record Credentials(String email, String password) {}

    // POST /api/auth/signup → create an account and log in
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Credentials creds, HttpServletRequest request, HttpServletResponse response) {
        String email = creds.email() == null ? "" : creds.email().trim().toLowerCase();
        String password = creds.password() == null ? "" : creds.password();

        if (!email.contains("@") || password.length() < 8) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Enter a valid email and a password of at least 8 characters."));
        }
        if (users.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "An account with that email already exists."));
        }

        users.save(new User(email, encoder.encode(password)));
        return login(new Credentials(email, password), request, response);
    }

    // POST /api/auth/login → check the password and start a session
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credentials creds, HttpServletRequest request, HttpServletResponse response) {
        String email = creds.email() == null ? "" : creds.email().trim().toLowerCase();
        String password = creds.password() == null ? "" : creds.password();

        try {
            Authentication auth = authManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(email, password));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            contextRepo.saveContext(context, request, response);  // remember the user in the session

            return ResponseEntity.ok(Map.of("email", auth.getName()));
        } catch (AuthenticationException e) {
            // Same message whether the email or the password was wrong, so attackers can't guess emails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Wrong email or password."));
        }
    }

    // GET /api/auth/me → who is logged in? (401 if nobody)
    @GetMapping("/me")
    public Map<String, String> me(Authentication auth) {
        return Map.of("email", auth.getName());
    }
}