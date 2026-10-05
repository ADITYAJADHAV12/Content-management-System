
package com.example.classroomcms.repository;

import com.example.classroomcms.entity.ContentPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContentPermissionRepository
        extends JpaRepository<ContentPermission, Long> {

    List<ContentPermission> findByClassroomIdAndGrantedTrue(Long classroomId);

    List<ContentPermission> findByContentId(Long contentId);

    Optional<ContentPermission> findByContentIdAndClassroomId(
            Long contentId,
            Long classroomId
    );
}
