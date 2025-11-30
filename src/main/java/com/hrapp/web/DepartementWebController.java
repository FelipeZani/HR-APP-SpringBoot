package com.hrapp.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hrapp.domain.Employee;
import com.hrapp.service.EmployeeService;

@Controller
public class DepartementWebController {

    private final EmployeeService employeeService;

    public DepartementWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    
    @GetMapping("/departements")
    public String listDepartements(Model model) {
        List<String> departementNames = employeeService.listAllDepartementNames(); 
        model.addAttribute("departements", departementNames);
        
        return "departements";
    }

    @GetMapping("/departements/{name}")
    public String departementDetails(@PathVariable("name") String departementName, Model model) {
        List<Employee> employees = employeeService.listEmployeesByDepartement(departementName);
        model.addAttribute("employees", employees);

        List<Employee> employeesToAssign = employeeService.listEmployeesToAssign(departementName);
        model.addAttribute("employeesToAssign", employeesToAssign);
        
        model.addAttribute("departementName", departementName);
        
        return "departement-details";
    }

    @PostMapping("/departements/{name}/assign")
    public String assignEmployee(
            @PathVariable("name") String departementName,
            @RequestParam("employeeId") Long employeeId,
            RedirectAttributes ra) {
        employeeService.assignEmployeeToDepartement(employeeId, departementName);
        ra.addFlashAttribute("successMessage", "Employé affecté avec succès !");
        
        return "redirect:/departements/" + departementName;
    }
    
    @PostMapping("/departements/{name}/unassign")
    public String unassignEmployee(
            @PathVariable("name") String departementName,
            @RequestParam("employeeId") Long employeeId,
            RedirectAttributes ra) {
        employeeService.unassignEmployee(employeeId);
        ra.addFlashAttribute("successMessage", "Employé désaffecté avec succès !");
        
        return "redirect:/departements/" + departementName;
    }
}