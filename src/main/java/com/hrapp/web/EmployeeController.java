package com.hrapp.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrapp.domain.Employee;
import com.hrapp.domain.Project;
import com.hrapp.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeController {
  private final EmployeeService service;
  public EmployeeController(EmployeeService service) { this.service = service; }

  @GetMapping public List<Employee> list() { return service.list(); }
  @GetMapping("/{id}") public Employee get(@PathVariable Long id) { return service.get(id); }
  @PostMapping public Employee create(@RequestBody Employee e) { return service.create(e); }
  @PutMapping("/{id}") public Employee update(@PathVariable Long id, @RequestBody Employee e) { return service.update(id, e); }
  @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }

  @GetMapping("/{id}/projects") 
  public List<Project> getEmployeeProjects(@PathVariable Long id) {
    return service.getProjectsByEmployeeId(id); 
}
}
