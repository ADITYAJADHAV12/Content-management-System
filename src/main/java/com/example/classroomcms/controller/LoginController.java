package com.example.classroomcms.controller;

import com.example.classroomcms.entity.User;
import com.example.classroomcms.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> loginRequest,
            HttpSession session) {

        String username =
                loginRequest.get("username");

        String password =
                loginRequest.get("password");

        if (username == null || password == null) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Username and password are required"
                    ));
        }

        Optional<User> userOptional =
                userService.login(username, password);

        if (userOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Invalid username or password"
                    ));
        }

        User user = userOptional.get();

        // Save logged-in user information in session
        session.setAttribute(
                "userId",
                user.getId()
        );

        session.setAttribute(
                "username",
                user.getUsername()
        );

        session.setAttribute(
                "role",
                user.getRole()
        );

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "message",
                "Login successful"
        );

        response.put(
                "id",
                user.getId()
        );

        response.put(
                "username",
                user.getUsername()
        );

        response.put(
                "name",
                user.getName()
        );

        response.put(
                "email",
                user.getEmail()
        );

        response.put(
                "role",
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Logout successful"
                )
        );
    }

    @GetMapping("/current")
    public ResponseEntity<?> currentUser(
            HttpSession session) {

        Object userId =
                session.getAttribute("userId");

        if (userId == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "No user is logged in"
                    ));
        }

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "id",
                session.getAttribute("userId")
        );

        response.put(
                "username",
                session.getAttribute("username")
        );

        response.put(
                "role",
                session.getAttribute("role")
        );

        return ResponseEntity.ok(response);
    }
}