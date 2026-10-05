package com.example.classroomcms.controller;

import com.example.classroomcms.entity.Classroom;
import com.example.classroomcms.service.ClassroomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/classroom")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    // =====================================================
    // GET ALL CLASSROOMS
    // Used by Teacher Dashboard
    // =====================================================

    @GetMapping("/list")
    public ResponseEntity<?> getAllClassrooms() {

        try {

            List<Classroom> classrooms =
                    classroomService.getAllClassrooms();

            return ResponseEntity.ok(classrooms);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message",
                            "Unable to load classrooms"
                    ));
        }
    }


    // =====================================================
    // CLASSROOM LOGIN CHECK
    // =====================================================

    private boolean isClassroom(HttpSession session) {

        Object role =
                session.getAttribute("role");

        return role != null
                && role.toString().equalsIgnoreCase("CLASSROOM");
    }


    // =====================================================
    // GET CURRENT CLASSROOM PROFILE
    // =====================================================

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            HttpSession session) {

        if (!isClassroom(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Classroom login required"
                    ));
        }

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "User session not found"
                    ));
        }

        Optional<Classroom> classroomOptional =
                classroomService
                        .findClassroomByUserId(userId);

        if (classroomOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            "Classroom profile not found"
                    ));
        }

        return ResponseEntity.ok(
                classroomOptional.get()
        );
    }


    // =====================================================
    // GET CONTENT ALLOWED FOR CURRENT CLASSROOM
    // =====================================================

    @GetMapping("/content")
    public ResponseEntity<?> getMyContent(
            HttpSession session) {

        if (!isClassroom(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Classroom login required"
                    ));
        }

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "User session not found"
                    ));
        }

        Optional<Classroom> classroomOptional =
                classroomService
                        .findClassroomByUserId(userId);

        if (classroomOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            "Classroom not found"
                    ));
        }

        Long classroomId =
                classroomOptional
                        .get()
                        .getId();

        return ResponseEntity.ok(
                classroomService
                        .getAllowedContent(classroomId)
        );
    }
}