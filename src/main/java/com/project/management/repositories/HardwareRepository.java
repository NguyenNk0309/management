package com.project.management.repositories;

import com.project.management.entities.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {
}

