package com.example.classroomcms.service;

import com.example.classroomcms.entity.Classroom;
import com.example.classroomcms.entity.Content;
import com.example.classroomcms.entity.ContentPermission;
import com.example.classroomcms.entity.Teacher;

import com.example.classroomcms.repository.ClassroomRepository;
import com.example.classroomcms.repository.ContentPermissionRepository;
import com.example.classroomcms.repository.ContentRepository;
import com.example.classroomcms.repository.TeacherRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContentService {

    private final ContentRepository contentRepository;

    private final TeacherRepository teacherRepository;

    private final ClassroomRepository classroomRepository;

    private final ContentPermissionRepository permissionRepository;

    /*
     * All uploaded files are stored here.
     *
     * It can contain:
     * - videos
     * - images
     * - PDFs
     * - other files
     */
    private final String uploadDirectory =
            "uploads/videos";

    public ContentService(

            ContentRepository contentRepository,

            TeacherRepository teacherRepository,

            ClassroomRepository classroomRepository,

            ContentPermissionRepository permissionRepository) {

        this.contentRepository =
                contentRepository;

        this.teacherRepository =
                teacherRepository;

        this.classroomRepository =
                classroomRepository;

        this.permissionRepository =
                permissionRepository;
    }

    // =========================================================
    // UPLOAD CONTENT
    // =========================================================

    public Content uploadContent(

            Long teacherId,

            String title,

            String description,

            MultipartFile file) throws IOException {

        // -----------------------------------------
        // Validate teacher
        // -----------------------------------------

        Optional<Teacher> teacherOptional =
                teacherRepository.findById(teacherId);

        if (teacherOptional.isEmpty()) {

            throw new RuntimeException(
                    "Teacher not found"
            );
        }

        Teacher teacher =
                teacherOptional.get();

        // -----------------------------------------
        // Validate file
        // -----------------------------------------

        if (file == null || file.isEmpty()) {

            throw new RuntimeException(
                    "Please select a file"
            );
        }

        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null
                || originalFileName.isBlank()) {

            throw new RuntimeException(
                    "Invalid file name"
            );
        }

        /*
         * Keep only the actual file name.
         *
         * This prevents someone from sending
         * a path such as:
         *
         * C:\something\file.mp4
         */
        originalFileName =
                Paths.get(originalFileName)
                        .getFileName()
                        .toString();

        // -----------------------------------------
        // Create upload directory
        // -----------------------------------------

        Path uploadPath =
                Paths.get(uploadDirectory)
                        .toAbsolutePath()
                        .normalize();

        if (!Files.exists(uploadPath)) {

            Files.createDirectories(uploadPath);
        }

        // -----------------------------------------
        // Get extension
        // -----------------------------------------

        String extension = "";

        int lastDot =
                originalFileName.lastIndexOf(".");

        if (lastDot >= 0) {

            extension =
                    originalFileName
                            .substring(lastDot);
        }

        // -----------------------------------------
        // Generate unique file name
        // -----------------------------------------

        String storedFileName =
                UUID.randomUUID()
                        + extension;

        Path filePath =
                uploadPath
                        .resolve(storedFileName)
                        .normalize();

        // -----------------------------------------
        // Save physical file
        // -----------------------------------------

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        // -----------------------------------------
        // Detect content type
        // -----------------------------------------

        String contentType =
                file.getContentType();

        if (contentType == null
                || contentType.isBlank()) {

            try {

                contentType =
                        Files.probeContentType(
                                filePath
                        );

            } catch (IOException ignored) {
            }
        }

        if (contentType == null
                || contentType.isBlank()) {

            contentType =
                    "application/octet-stream";
        }

        // -----------------------------------------
        // Create Content entity
        // -----------------------------------------

        Content content =
                new Content();

        content.setTitle(title);

        content.setDescription(description);

        /*
         * Original file name is stored in database.
         *
         * Example:
         * lecture.mp4
         * java.png
         * notes.pdf
         */
        content.setFileName(
                originalFileName
        );

        /*
         * Actual physical file name is unique.
         *
         * Example:
         * 550e8400-e29b-41d4-a716-446655440000.mp4
         */
        content.setFilePath(
                filePath.toString()
        );

        /*
         * Examples:
         *
         * video/mp4
         * image/jpeg
         * image/png
         * application/pdf
         */
        content.setContentType(
                contentType
        );

        content.setUploadedBy(
                teacher
        );

        return contentRepository.save(
                content
        );
    }

    // =========================================================
    // GET ALL CONTENT
    // =========================================================

    public List<Content> getAllContent() {

        return contentRepository.findAll();
    }

    // =========================================================
    // GET CONTENT BY ID
    // =========================================================

    public Optional<Content> getContentById(
            Long id) {

        return contentRepository.findById(id);
    }

    // =========================================================
    // GET TEACHER CONTENT
    // =========================================================

    public List<Content> getTeacherContent(
            Long teacherId) {

        Optional<Teacher> teacherOptional =
                teacherRepository.findById(
                        teacherId
                );

        if (teacherOptional.isEmpty()) {

            throw new RuntimeException(
                    "Teacher not found"
            );
        }

        return contentRepository
                .findByUploadedBy(
                        teacherOptional.get()
                );
    }

    // =========================================================
    // UPDATE CONTENT
    // =========================================================

    public Content updateContent(

            Long contentId,

            String title,

            String description) {

        Optional<Content> contentOptional =
                contentRepository.findById(
                        contentId
                );

        if (contentOptional.isEmpty()) {

            throw new RuntimeException(
                    "Content not found"
            );
        }

        Content content =
                contentOptional.get();

        if (title != null
                && !title.isBlank()) {

            content.setTitle(title);
        }

        if (description != null) {

            content.setDescription(
                    description
            );
        }

        return contentRepository.save(
                content
        );
    }

    // =========================================================
    // DELETE CONTENT
    // =========================================================

    public String deleteContent(
            Long contentId) {

        Optional<Content> contentOptional =
                contentRepository.findById(
                        contentId
                );

        if (contentOptional.isEmpty()) {

            throw new RuntimeException(
                    "Content not found"
            );
        }

        Content content =
                contentOptional.get();

        // -----------------------------------------
        // Delete physical file
        // -----------------------------------------

        if (content.getFilePath() != null
                && !content.getFilePath().isBlank()) {

            try {

                Path path =
                        Paths.get(
                                content.getFilePath()
                        );

                Files.deleteIfExists(path);

            } catch (IOException e) {

                System.out.println(
                        "Could not delete physical file: "
                                + e.getMessage()
                );
            }
        }

        // -----------------------------------------
        // Delete permissions
        // -----------------------------------------

        List<ContentPermission> permissions =
                permissionRepository
                        .findByContentId(
                                contentId
                        );

        permissionRepository.deleteAll(
                permissions
        );

        // -----------------------------------------
        // Delete database record
        // -----------------------------------------

        contentRepository.delete(
                content
        );

        return "Content deleted successfully";
    }

    // =========================================================
    // GRANT PERMISSION
    // =========================================================

    public ContentPermission grantPermission(

            Long contentId,

            Long classroomId) {

        Optional<Content> contentOptional =
                contentRepository.findById(
                        contentId
                );

        if (contentOptional.isEmpty()) {

            throw new RuntimeException(
                    "Content not found"
            );
        }

        Optional<Classroom> classroomOptional =
                classroomRepository.findById(
                        classroomId
                );

        if (classroomOptional.isEmpty()) {

            throw new RuntimeException(
                    "Classroom not found"
            );
        }

        // -----------------------------------------
        // Check existing permission
        // -----------------------------------------

        Optional<ContentPermission>
                existingPermission =
                permissionRepository
                        .findByContentIdAndClassroomId(
                                contentId,
                                classroomId
                        );

        if (existingPermission.isPresent()) {

            ContentPermission permission =
                    existingPermission.get();

            permission.setGranted(true);

            return permissionRepository.save(
                    permission
            );
        }

        // -----------------------------------------
        // Create new permission
        // -----------------------------------------

        ContentPermission permission =
                new ContentPermission();

        permission.setContent(
                contentOptional.get()
        );

        permission.setClassroom(
                classroomOptional.get()
        );

        permission.setGranted(true);

        return permissionRepository.save(
                permission
        );
    }

    // =========================================================
    // REVOKE PERMISSION
    // =========================================================

    public String revokePermission(

            Long contentId,

            Long classroomId) {

        Optional<ContentPermission>
                permissionOptional =
                permissionRepository
                        .findByContentIdAndClassroomId(
                                contentId,
                                classroomId
                        );

        if (permissionOptional.isEmpty()) {

            throw new RuntimeException(
                    "Permission does not exist"
            );
        }

        ContentPermission permission =
                permissionOptional.get();

        permission.setGranted(false);

        permissionRepository.save(
                permission
        );

        return "Permission revoked successfully";
    }

    // =========================================================
    // GET CONTENT PERMISSIONS
    // =========================================================

    public List<ContentPermission>
    getContentPermissions(
            Long contentId) {

        return permissionRepository
                .findByContentId(
                        contentId
                );
    }

    // =========================================================
    // GET CLASSROOM CONTENT
    // =========================================================

    public List<ContentPermission>
    getClassroomContent(
            Long classroomId) {

        return permissionRepository
                .findByClassroomIdAndGrantedTrue(
                        classroomId
                );
    }

    // =========================================================
    // CHECK WHETHER USER CAN VIEW CONTENT
    // =========================================================

    public boolean canViewContent(

            Long contentId,

            HttpSession session) {

        Object roleObject =
                session.getAttribute("role");

        Object userIdObject =
                session.getAttribute("userId");

        if (roleObject == null
                || userIdObject == null) {

            return false;
        }

        String role =
                roleObject.toString();

        Long userId =
                (Long) userIdObject;

        // =====================================================
        // TEACHER
        // =====================================================

        if (role.equalsIgnoreCase("TEACHER")) {

            Optional<Teacher> teacherOptional =
                    teacherRepository.findAll()
                            .stream()
                            .filter(teacher ->
                                    teacher.getUser() != null
                                    && teacher.getUser()
                                            .getId()
                                            .equals(userId)
                            )
                            .findFirst();

            if (teacherOptional.isEmpty()) {

                return false;
            }

            Optional<Content> contentOptional =
                    contentRepository.findById(
                            contentId
                    );

            if (contentOptional.isEmpty()) {

                return false;
            }

            Content content =
                    contentOptional.get();

            return content.getUploadedBy() != null
                    && content.getUploadedBy()
                            .getId()
                            .equals(
                                    teacherOptional
                                            .get()
                                            .getId()
                            );
        }

        // =====================================================
        // CLASSROOM
        // =====================================================

        if (role.equalsIgnoreCase("CLASSROOM")) {

            Optional<Classroom> classroomOptional =
                    classroomRepository.findAll()
                            .stream()
                            .filter(classroom ->
                                    classroom.getUser() != null
                                    && classroom.getUser()
                                            .getId()
                                            .equals(userId)
                            )
                            .findFirst();

            if (classroomOptional.isEmpty()) {

                return false;
            }

            Long classroomId =
                    classroomOptional
                            .get()
                            .getId();

            Optional<ContentPermission>
                    permissionOptional =
                    permissionRepository
                            .findByContentIdAndClassroomId(
                                    contentId,
                                    classroomId
                            );

            return permissionOptional
                    .map(ContentPermission::isGranted)
                    .orElse(false);
        }

        return false;
    }
}