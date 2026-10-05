package com.example.classroomcms.service;

import com.example.classroomcms.entity.Classroom;
import com.example.classroomcms.entity.Teacher;
import com.example.classroomcms.entity.User;
import com.example.classroomcms.repository.ClassroomRepository;
import com.example.classroomcms.repository.TeacherRepository;
import com.example.classroomcms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    public AdminService(UserRepository userRepository,
                        TeacherRepository teacherRepository,
                        ClassroomRepository classroomRepository) {

        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.classroomRepository = classroomRepository;
    }

    // =========================
    // TEACHER METHODS
    // =========================

    public Teacher addTeacher(String username,
                              String password,
                              String name,
                              String email,
                              String specialization) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setRole("TEACHER");
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        Teacher teacher = new Teacher();

        teacher.setUser(savedUser);
        teacher.setSpecialization(specialization);

        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Teacher updateTeacher(Long id,
                                 String username,
                                 String password,
                                 String name,
                                 String email,
                                 String specialization) {

        Optional<Teacher> teacherOptional =
                teacherRepository.findById(id);

        if (teacherOptional.isEmpty()) {
            throw new RuntimeException("Teacher not found");
        }

        Teacher teacher = teacherOptional.get();

        User user = teacher.getUser();

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }

        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }

        if (name != null) {
            user.setName(name);
        }

        if (email != null) {
            user.setEmail(email);
        }

        if (specialization != null) {
            teacher.setSpecialization(specialization);
        }

        userRepository.save(user);

        return teacherRepository.save(teacher);
    }

    public String deleteTeacher(Long id) {

        Optional<Teacher> teacherOptional =
                teacherRepository.findById(id);

        if (teacherOptional.isEmpty()) {
            throw new RuntimeException("Teacher not found");
        }

        Teacher teacher = teacherOptional.get();

        User user = teacher.getUser();

        // Soft delete
        user.setEnabled(false);

        userRepository.save(user);

        return "Teacher disabled successfully";
    }

    // =========================
    // CLASSROOM METHODS
    // =========================

    public Classroom addClassroom(String username,
                                  String password,
                                  String name,
                                  String email,
                                  String classroomName,
                                  String description) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setRole("CLASSROOM");
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        Classroom classroom = new Classroom();

        classroom.setUser(savedUser);
        classroom.setName(classroomName);
        classroom.setDescription(description);
        classroom.setEnabled(true);

        return classroomRepository.save(classroom);
    }

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Optional<Classroom> getClassroomById(Long id) {
        return classroomRepository.findById(id);
    }

    public Classroom updateClassroom(Long id,
                                     String username,
                                     String password,
                                     String name,
                                     String email,
                                     String classroomName,
                                     String description) {

        Optional<Classroom> classroomOptional =
                classroomRepository.findById(id);

        if (classroomOptional.isEmpty()) {
            throw new RuntimeException("Classroom not found");
        }

        Classroom classroom = classroomOptional.get();

        User user = classroom.getUser();

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }

        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }

        if (name != null) {
            user.setName(name);
        }

        if (email != null) {
            user.setEmail(email);
        }

        if (classroomName != null) {
            classroom.setName(classroomName);
        }

        if (description != null) {
            classroom.setDescription(description);
        }

        userRepository.save(user);

        return classroomRepository.save(classroom);
    }

    public String deleteClassroom(Long id) {

        Optional<Classroom> classroomOptional =
                classroomRepository.findById(id);

        if (classroomOptional.isEmpty()) {
            throw new RuntimeException("Classroom not found");
        }

        Classroom classroom = classroomOptional.get();

        classroom.setEnabled(false);

        User user = classroom.getUser();

        user.setEnabled(false);

        classroomRepository.save(classroom);
        userRepository.save(user);

        return "Classroom disabled successfully";
    }
}