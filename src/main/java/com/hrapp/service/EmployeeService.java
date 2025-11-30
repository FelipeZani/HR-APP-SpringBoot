package com.hrapp.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hrapp.domain.Employee;
import com.hrapp.domain.Project;
import com.hrapp.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {
  private final EmployeeRepository repo;
  public EmployeeService(EmployeeRepository repo) { this.repo = repo; }

  public List<Employee> list() { return repo.findAll(); }
  public Employee get(Long id) { return repo.findById(id).orElseThrow(); }
  public Employee create(Employee e) { return repo.save(e); }
  public Employee update(Long id, Employee e) {
    Employee cur = get(id);
    cur.setFirstName(e.getFirstName());
    cur.setLastName(e.getLastName());
    cur.setEmail(e.getEmail());
    cur.setMatricule(e.getMatricule());
    cur.setDepartement(e.getDepartement());
    return repo.save(cur);
  }
  public void delete(Long id) { repo.deleteById(id); }

  public List<String> listAllDepartementNames() {
        return repo.findAllDistinctDepartementNames();
  }

  public List<Employee> listEmployeesByDepartement(String departementName) {
        return repo.findByDepartement(departementName);
  }

  public List<Employee> listEmployeesToAssign(String currentDepartementName) {
        return repo.findEmployeesNotByDepartement(currentDepartementName);
    }

  public Employee assignEmployeeToDepartement(Long employeeId, String departementName) {
        Employee employee = repo.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé: " + employeeId)); 
        
        employee.setDepartement(departementName);
        return repo.save(employee);
    }

  @Transactional
    public Employee unassignEmployee(Long employeeId) {
        Employee employee = repo.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé: " + employeeId)); 
        
        employee.setDepartement(null);
        return repo.save(employee);
    }


    @Transactional
    public List<Project> getProjectsByEmployeeId(Long id) {
        Employee employee = get(id);
        
        return employee.getProjects(); 
    }
    @Transactional
    public Map<String, Long> getEmployeeCountByDepartement() {
            List<Object[]> results = repo.countEmployeesByDepartement();
            return results.stream()
                          .collect(Collectors.toMap(
                              result -> (String) result[0],
                              result -> (Long) result[1]
                          ));
    }
}
