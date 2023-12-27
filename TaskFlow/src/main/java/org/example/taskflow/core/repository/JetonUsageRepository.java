package org.example.taskflow.core.repository;

import org.example.taskflow.core.model.entity.JetonUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JetonUsageRepository extends JpaRepository<JetonUsage, Long> {
}
