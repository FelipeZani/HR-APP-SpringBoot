package com.hrapp.web;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hrapp.domain.Employee;
import com.hrapp.domain.Project;
import com.hrapp.service.EmployeeService;
import com.hrapp.service.ProjectService;

@Controller
@RequestMapping("/projects")
public class ProjectWebController {
    
    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public ProjectWebController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    

    //Lister les projets
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.list());
        return "projects/list";
    }
    
    
    @GetMapping("/new")
    public String newProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "projects/form";
    }
    
    // 3. Créer / Modifier un projet
    @PostMapping("/save")
    public String saveProject(@ModelAttribute Project project, RedirectAttributes ra) {
        if (project.getId() == null) {
            projectService.create(project);
            ra.addFlashAttribute("successMessage", "Projet créé avec succès !");
        } else {
            projectService.update(project.getId(), project);
            ra.addFlashAttribute("successMessage", "Projet mis à jour avec succès !");
        }
        return "redirect:/projects";
    }

    // 4. Afficher le formulaire de modification
    @GetMapping("/edit/{id}")
    public String editProjectForm(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.get(id));
        return "projects/form";
    }

    // 5. Supprimer un projet
    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id, RedirectAttributes ra) {
        projectService.delete(id);
        ra.addFlashAttribute("successMessage", "Projet supprimé avec succès !");
        return "redirect:/projects";
    }

    // --- Gestion des Affectations ---

    // 6. Page de détails du projet et d'affectation
    @GetMapping("/{id}/details")
    public String projectDetails(@PathVariable Long id, Model model) {
        Project project = projectService.get(id);
        model.addAttribute("project", project);

        // Employés qui ne sont PAS dans ce projet (pour l'affectation)
        List<Employee> allEmployees = employeeService.list();
        List<Employee> employeesNotInProject = allEmployees.stream()
            .filter(e -> !project.getEmployees().contains(e))
            .toList();
            
        model.addAttribute("employeesNotInProject", employeesNotInProject);
        
        // Liste des statuts (pour simplicité)
        model.addAttribute("statuses", List.of("TODO", "IN_PROGRESS", "COMPLETED"));

        return "projects/details"; // Mappe sur src/main/resources/templates/projects/details.html
    }

    // 7. Affecter un employé
    @PostMapping("/{id}/assign")
    public String assignEmployee(
            @PathVariable Long id, 
            @RequestParam Long employeeId, 
            RedirectAttributes ra) {
        projectService.addEmployeeToProject(id, employeeId);
        ra.addFlashAttribute("successMessage", "Employé affecté au projet.");
        return "redirect:/projects/" + id + "/details";
    }
    
    // 8. Désaffecter un employé
    @PostMapping("/{id}/unassign")
    public String unassignEmployee(
            @PathVariable Long id, 
            @RequestParam Long employeeId, 
            RedirectAttributes ra) {
        projectService.removeEmployeeFromProject(id, employeeId);
        ra.addFlashAttribute("successMessage", "Employé désaffecté du projet.");
        return "redirect:/projects/" + id + "/details";
    }
}