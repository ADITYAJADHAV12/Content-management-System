package com.example.classroomcms.controller;

import com.example.classroomcms.entity.Classroom;
import com.example.classroomcms.entity.Teacher;
import com.example.classroomcms.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // =========================
    // ADMIN CHECK
    // =========================

    private boolean isAdmin(HttpSession session) {

        Object role =
                session.getAttribute("role");

        return role != null
                && role.toString().equals("ADMIN");
    }

    // =========================
    // ADD TEACHER
    // =========================

    @PostMapping("/teachers")
    public ResponseEntity<?> addTeacher(
            @RequestBody Map<String, String> request,
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        try {

            Teacher teacher =
                    adminService.addTeacher(
                            request.get("username"),
                            request.get("password"),
                            request.get("name"),
                            request.get("email"),
                            request.get("specialization")
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(teacher);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // =========================
    // GET ALL TEACHERS
    // =========================

    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers(
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        return ResponseEntity.ok(
                adminService.getAllTeachers()
        );
    }

    // =========================
    // GET TEACHER
    // =========================

    @GetMapping("/teachers/{id}")
    public ResponseEntity<?> getTeacher(
            @PathVariable Long id,
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        return adminService
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

    // =========================
    // UPDATE TEACHER
    // =========================

    @PutMapping("/teachers/{id}")
    public ResponseEntity<?> updateTeacher(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        try {

            Teacher teacher =
                    adminService.updateTeacher(
                            id,
                            request.get("username"),
                            request.get("password"),
                            request.get("name"),
                            request.get("email"),
                            request.get("specialization")
                    );

            return ResponseEntity.ok(teacher);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // =========================
    // DELETE / DISABLE TEACHER
    // =========================

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<?> deleteTeacher(
            @PathVariable Long id,
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        try {

            String message =
                    adminService.deleteTeacher(id);

            return ResponseEntity.ok(
                    Map.of("message", message)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // =========================
    // ADD CLASSROOM
    // =========================

    @PostMapping("/classrooms")
    public ResponseEntity<?> addClassroom(
            @RequestBody Map<String, String> request,
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        try {

            Classroom classroom =
                    adminService.addClassroom(
                            request.get("username"),
                            request.get("password"),
                            request.get("name"),
                            request.get("email"),
                            request.get("classroomName"),
                            request.get("description")
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(classroom);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // =========================
    // GET ALL CLASSROOMS
    // =========================

    @GetMapping("/classrooms")
    public ResponseEntity<?> getClassrooms(
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        return ResponseEntity.ok(
                adminService.getAllClassrooms()
        );
    }

    // =========================
    // GET CLASSROOM
    // =========================

    @GetMapping("/classrooms/{id}")
    public ResponseEntity<?> getClassroom(
            @PathVariable Long id,
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        return adminService
                .getClassroomById(id)
                .<ResponseEntity<?>>map(
                        ResponseEntity::ok
                )
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of(
                                        "message",
                                        "Classroom not found"
                                ))
                );
    }

    // =========================
    // UPDATE CLASSROOM
    // =========================

    @PutMapping("/classrooms/{id}")
    public ResponseEntity<?> updateClassroom(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        try {

            Classroom classroom =
                    adminService.updateClassroom(
                            id,
                            request.get("username"),
                            request.get("password"),
                            request.get("name"),
                            request.get("email"),
                            request.get("classroomName"),
                            request.get("description")
                    );

            return ResponseEntity.ok(classroom);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // =========================
    // DELETE / DISABLE CLASSROOM
    // =========================

    @DeleteMapping("/classrooms/{id}")
    public ResponseEntity<?> deleteClassroom(
            @PathVariable Long id,
            HttpSession session) {

        if (!isAdmin(session)) {
            return unauthorized();
        }

        try {

            String message =
                    adminService.deleteClassroom(id);

            return ResponseEntity.ok(
                    Map.of("message", message)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // =========================
    // UNAUTHORIZED RESPONSE
    // =========================

    private ResponseEntity<?> unauthorized() {

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "message",
                "Admin login required"
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }
}