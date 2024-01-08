package org.example.taskflow.core.repository;

import org.example.taskflow.core.model.entity.Role;
import org.example.taskflow.core.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String tagName);

}
