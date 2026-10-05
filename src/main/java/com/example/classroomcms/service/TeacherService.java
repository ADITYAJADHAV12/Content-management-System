package com.example.classroomcms.service;

import com.example.classroomcms.entity.Teacher;
import com.example.classroomcms.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> findTeacherByUserId(Long userId) {

        List<Teacher> teachers = teacherRepository.findAll();

        for (Teacher teacher : teachers) {

            if (teacher.getUser() != null
                    && teacher.getUser().getId().equals(userId)) {

                return Optional.of(teacher);
            }
        }

        return Optional.empty();
    }
}