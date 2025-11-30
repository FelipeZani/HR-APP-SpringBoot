package com.hrapp.repository;

import java.util.Date;
import java.util.List;

import com.hrapp.domain.Paystub;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaystubRepository extends JpaRepository<Paystub,Long> {
    List<Paystub> findByEmployeeIdOrderByPayPeriodDesc(Long employeeId);
    List<Paystub> findByPayPeriodBetween(Date start, Date end);
    List<Paystub> findByEmployeeIdAndPayPeriodBetween(Long employeeId, Date start, Date end);
    
}
