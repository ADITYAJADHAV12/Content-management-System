package com.example.classroomcms.service;

import com.example.classroomcms.entity.Classroom;
import com.example.classroomcms.entity.ContentPermission;
import com.example.classroomcms.repository.ClassroomRepository;
import com.example.classroomcms.repository.ContentPermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    private final ContentPermissionRepository contentPermissionRepository;

    public ClassroomService(
            ClassroomRepository classroomRepository,
            ContentPermissionRepository contentPermissionRepository) {

        this.classroomRepository =
                classroomRepository;

        this.contentPermissionRepository =
                contentPermissionRepository;
    }


    // =====================================================
    // GET ALL CLASSROOMS
    // =====================================================

    public List<Classroom> getAllClassrooms() {

        return classroomRepository.findAll();
    }


    // =====================================================
    // GET CLASSROOM BY ID
    // =====================================================

    public Optional<Classroom> getClassroomById(
            Long id) {

        return classroomRepository.findById(id);
    }


    // =====================================================
    // FIND CLASSROOM BY USER ID
    // =====================================================

    public Optional<Classroom> findClassroomByUserId(
            Long userId) {

        List<Classroom> classrooms =
                classroomRepository.findAll();

        for (Classroom classroom : classrooms) {

            if (classroom.getUser() != null
                    && classroom.getUser()
                            .getId()
                            .equals(userId)) {

                return Optional.of(classroom);
            }
        }

        return Optional.empty();
    }


    // =====================================================
    // GET ALLOWED CONTENT
    // =====================================================

    public List<ContentPermission> getAllowedContent(
            Long classroomId) {

        return contentPermissionRepository
                .findByClassroomIdAndGrantedTrue(
                        classroomId
                );
    }
}