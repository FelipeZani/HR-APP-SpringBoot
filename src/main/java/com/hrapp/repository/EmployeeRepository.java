package com.hrapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hrapp.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  boolean existsByEmail(String email);
  List<Employee> findByDepartement(String departement);

  @Query("SELECT DISTINCT e.departement FROM Employee e WHERE e.departement IS NOT NULL AND e.departement != ''")
  List<String> findAllDistinctDepartementNames();

  @Query("SELECT e FROM Employee e WHERE e.departement IS NULL OR e.departement = '' OR e.departement != ?1")
  List<Employee> findEmployeesNotByDepartement(String departement);

  @Query("SELECT e.departement, COUNT(e) FROM Employee e WHERE e.departement IS NOT NULL AND e.departement != '' GROUP BY e.departement")
  List<Object[]> countEmployeesByDepartement();
}
