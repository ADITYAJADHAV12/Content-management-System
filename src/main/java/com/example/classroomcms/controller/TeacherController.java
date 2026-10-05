package com.example.classroomcms.controller;

import com.example.classroomcms.entity.Teacher;
import com.example.classroomcms.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // =========================
    // TEACHER CHECK
    // =========================

    private boolean isTeacher(HttpSession session) {

        Object role =
                session.getAttribute("role");

        return role != null
                && role.toString().equals("TEACHER");
    }

    // =========================
    // GET CURRENT TEACHER
    // =========================

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            HttpSession session) {

        if (!isTeacher(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Teacher login required"
                    ));
        }

        Long userId =
                (Long) session.getAttribute("userId");

        Optional<Teacher> teacherOptional =
                teacherService.findTeacherByUserId(userId);

        if (teacherOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            "Teacher profile not found"
                    ));
        }

        return ResponseEntity.ok(
                teacherOptional.get()
        );
    }

    // =========================
    // GET TEACHER BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacher(
            @PathVariable Long id,
            HttpSession session) {

        if (!isTeacher(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Teacher login required"
                    ));
        }

        return teacherService
                .getTeacherById(id)
                .<ResponseEntity<?>>map(
                        ResponseEntity::ok
                )
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of(
                                        "message",
                                        "Teacher not found"
                                ))
                );
    }
}