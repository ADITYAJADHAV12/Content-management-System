package com.example.classroomcms.controller;

import com.example.classroomcms.entity.Content;
import com.example.classroomcms.entity.ContentPermission;
import com.example.classroomcms.entity.Teacher;
import com.example.classroomcms.service.ContentService;

import com.example.classroomcms.service.TeacherService;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService contentService;
    private final TeacherService teacherService;

    public ContentController(
            ContentService contentService,
            TeacherService teacherService) {

        this.contentService = contentService;
        this.teacherService = teacherService;
    }

    // =========================================================
    // CHECK TEACHER LOGIN
    // =========================================================

    private boolean isTeacher(HttpSession session) {

        Object role = session.getAttribute("role");

        return role != null
                && role.toString().equalsIgnoreCase("TEACHER");
    }

    // =========================================================
    // UPLOAD CONTENT
    // =========================================================

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadContent(

            @RequestParam("title")
            String title,

            @RequestParam(
                    value = "description",
                    required = false
            )
            String description,

            @RequestParam("file")
            MultipartFile file,

            HttpSession session) {

        if (!isTeacher(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Teacher login required"
                    ));
        }

        try {

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

            Content content =
                    contentService.uploadContent(
                            teacherOptional.get().getId(),
                            title,
                            description,
                            file
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(content);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage() != null
                                    ? e.getMessage()
                                    : "File upload failed"
                    ));
        }
    }

    // =========================================================
    // GET TEACHER'S CONTENT
    // =========================================================

    @GetMapping("/my")
    public ResponseEntity<?> getMyContent(
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

        if (userId == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "User session not found"
                    ));
        }

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
                contentService.getTeacherContent(
                        teacherOptional.get().getId()
                )
        );
    }

    // =========================================================
    // VIEW / DOWNLOAD FILE
    //
    // This endpoint is used by classroom-dashboard.html.
    //
    // Images -> browser displays image
    // Videos -> browser displays playable video
    // PDF    -> browser opens PDF
    // =========================================================

    @GetMapping("/file/{id}")
    public ResponseEntity<?> viewFile(

            @PathVariable Long id,

            HttpSession session) {

        Optional<Content> contentOptional =
                contentService.getContentById(id);

        if (contentOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            "Content not found"
                    ));
        }

        Content content =
                contentOptional.get();

        // Check whether logged-in user can access this content
        if (!contentService.canViewContent(id, session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "You do not have permission to view this content"
                    ));
        }

        if (content.getFilePath() == null
                || content.getFilePath().isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            "File path not found"
                    ));
        }

        try {

            Path filePath =
                    Paths.get(content.getFilePath());

            if (!Files.exists(filePath)
                    || !Files.isRegularFile(filePath)) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "message",
                                "Physical file not found"
                        ));
            }

            Resource resource =
                    new FileSystemResource(filePath);

            String contentType =
                    content.getContentType();

            MediaType mediaType;

            try {

                if (contentType != null
                        && !contentType.isBlank()) {

                    mediaType =
                            MediaType.parseMediaType(
                                    contentType
                            );

                } else {

                    String detectedType =
                            Files.probeContentType(filePath);

                    if (detectedType != null) {

                        mediaType =
                                MediaType.parseMediaType(
                                        detectedType
                                );

                    } else {

                        mediaType =
                                MediaType.APPLICATION_OCTET_STREAM;
                    }
                }

            } catch (Exception e) {

                mediaType =
                        MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()

                    .contentType(mediaType)

                    .contentLength(
                            Files.size(filePath)
                    )

                    .header(
                            "Accept-Ranges",
                            "bytes"
                    )

                    .header(
                            "Content-Disposition",
                            ContentDisposition
                                    .inline()
                                    .filename(
                                            content.getFileName(),
                                            StandardCharsets.UTF_8
                                    )
                                    .build()
                                    .toString()
                    )

                    .body(resource);

        } catch (IOException e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(Map.of(
                            "message",
                            "Unable to open file"
                    ));
        }
    }

    // =========================================================
    // GET SINGLE CONTENT
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getContent(

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

        return contentService
                .getContentById(id)

                .<ResponseEntity<?>>map(
                        ResponseEntity::ok
                )

                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of(
                                        "message",
                                        "Content not found"
                                ))
                );
    }

    // =========================================================
    // UPDATE CONTENT DETAILS
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContent(

            @PathVariable Long id,

            @RequestBody Map<String, String> request,

            HttpSession session) {

        if (!isTeacher(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Teacher login required"
                    ));
        }

        try {

            Content content =
                    contentService.updateContent(
                            id,
                            request.get("title"),
                            request.get("description")
                    );

            return ResponseEntity.ok(content);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // =========================================================
    // DELETE CONTENT
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContent(

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

        try {

            String message =
                    contentService.deleteContent(id);

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

    // =========================================================
    // GRANT PERMISSION
    // =========================================================

    @PostMapping(
            "/{contentId}/permissions/{classroomId}"
    )
    public ResponseEntity<?> grantPermission(

            @PathVariable Long contentId,

            @PathVariable Long classroomId,

            HttpSession session) {

        if (!isTeacher(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Teacher login required"
                    ));
        }

        try {

            ContentPermission permission =
                    contentService.grantPermission(
                            contentId,
                            classroomId
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(permission);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // =========================================================
    // REVOKE PERMISSION
    // =========================================================

    @DeleteMapping(
            "/{contentId}/permissions/{classroomId}"
    )
    public ResponseEntity<?> revokePermission(

            @PathVariable Long contentId,

            @PathVariable Long classroomId,

            HttpSession session) {

        if (!isTeacher(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Teacher login required"
                    ));
        }

        try {

            String message =
                    contentService.revokePermission(
                            contentId,
                            classroomId
                    );

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

    // =========================================================
    // GET PERMISSIONS
    // =========================================================

    @GetMapping(
            "/{contentId}/permissions"
    )
    public ResponseEntity<?> getPermissions(

            @PathVariable Long contentId,

            HttpSession session) {

        if (!isTeacher(session)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message",
                            "Teacher login required"
                    ));
        }

        List<ContentPermission> permissions =
                contentService.getContentPermissions(
                        contentId
                );

        return ResponseEntity.ok(permissions);
    }
}