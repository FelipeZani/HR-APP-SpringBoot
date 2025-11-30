package com.hrapp.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hrapp.domain.Employee;
import com.hrapp.domain.Project;
import com.hrapp.repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectService {
    private final ProjectRepository repo;
    private final EmployeeService employeeService;
    public ProjectService(ProjectRepository repo, EmployeeService employeeService) {
         this.repo = repo;
         this.employeeService = employeeService; 
    }

    public List<Project> list() { return repo.findAll(); }
    public Project get(Long id) { return repo.findById(id).orElseThrow(); }
    public Project create(Project p) { return repo.save(p); }
    public Project update(Long id, Project p){
        Project cur = get(id);
        cur.setLabel(p.getLabel());
        cur.setStatus(p.getStatus());
        return repo.save(cur);
    }

    public void delete(Long id) { repo.deleteById(id); }


    @Transactional
    public Project addEmployeeToProject(Long projectId, Long employeeId) {
        Project project = get(projectId);
        
        Employee employee = employeeService.get(employeeId); 
        
        if (!project.getEmployees().contains(employee)) {
            project.getEmployees().add(employee);
        }
        return repo.save(project);
    }

    @Transactional
    public Project removeEmployeeFromProject(Long projectId, Long employeeId) {
        Project project = get(projectId);
        
        
        project.getEmployees().removeIf(e -> e.getId().equals(employeeId)); 
        
        return repo.save(project);
    }

        @Transactional
        public Map<String, Long> getProjectCountByStatus() {
                List<Object[]> results = repo.countProjectsByStatus();
                return results.stream()
                            .collect(Collectors.toMap(
                                result -> (String) result[0],
                                result -> (Long) result[1]
                            ));
            }
}
