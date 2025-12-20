package com.hrapp.repository;

import com.hrapp.domain.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartementRepository extends JpaRepository<Departement, Long>{
    
}