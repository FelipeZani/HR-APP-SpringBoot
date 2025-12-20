package com.hrapp.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hrapp.domain.Project;
import com.hrapp.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin
public class ProjectController {
    private final ProjectService service;
    public ProjectController(ProjectService service) { this.service = service; }

    @GetMapping public List<Project> list() { return service.list(); }
    @PostMapping public Project create(@RequestBody Project p) { return service.create(p); }
    @PutMapping("/{id}") public Project update(@PathVariable Long id, @RequestBody Project p) { return service.update(id, p); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { service.delete(id); }
}
