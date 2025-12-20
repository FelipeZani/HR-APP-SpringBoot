package com.hrapp.repository;

import com.hrapp.domain.Project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long>{
    @Query("SELECT p.status, COUNT(p) FROM Project p GROUP BY p.status")
    List<Object[]> countProjectsByStatus();
}
