
package com.example.classroomcms.repository;

import com.example.classroomcms.entity.Content;
import com.example.classroomcms.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByUploadedBy(Teacher teacher);
}
