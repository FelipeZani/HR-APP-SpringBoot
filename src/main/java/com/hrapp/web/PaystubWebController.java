package com.hrapp.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hrapp.domain.Employee;
import com.hrapp.domain.Paystub;
import com.hrapp.service.EmployeeService;
import com.hrapp.service.PaystubService;

@Controller
@RequestMapping("/paystubs")
public class PaystubWebController {

    private final PaystubService paystubService;
    private final EmployeeService employeeService; 

    public PaystubWebController(PaystubService paystubService, EmployeeService employeeService) {
        this.paystubService = paystubService;
        this.employeeService = employeeService;
    }

    
    @GetMapping
    public String listAndSearchPaystubs(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") Date startDate,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") Date endDate,
        Model model) {
        
        List<Paystub> paystubs = paystubService.searchPaystubs(employeeId, startDate, endDate);

        List<Employee> allEmployees = employeeService.list();
        Map<Long, Employee> employeeMap = allEmployees.stream()
            .collect(Collectors.toMap(Employee::getId, employee -> employee));
        
        model.addAttribute("paystubs", paystubs);
        model.addAttribute("employees", allEmployees);
        model.addAttribute("employeeMap", employeeMap);
        
        
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "paystubs/list"; 
    }
    
    
    @GetMapping("/new")
    public String newPaystubForm(Model model) {
        model.addAttribute("paystub", new Paystub());
        model.addAttribute("employees", employeeService.list());
        return "paystubs/form";
    }

    @PostMapping("/create")
    public String createPaystub(@ModelAttribute Paystub paystub, RedirectAttributes ra) {
        paystubService.create(paystub);
        ra.addFlashAttribute("successMessage", "Fiche de paie créée et calculée. Net : " + paystub.getNetTotal());
        return "redirect:/paystubs";
    }
    
    
    @GetMapping("/{id}")
    public String viewPaystubDetails(@PathVariable Long id, Model model) {
        Paystub paystub = paystubService.get(id);
        
        Employee employee = employeeService.get(paystub.getEmployeeId()); 
        
        model.addAttribute("paystub", paystub);
        model.addAttribute("employee", employee);
        return "paystubs/details"; 
    }

    
@GetMapping("/print/{id}")
    public ResponseEntity<Resource> generatePrint(@PathVariable Long id) throws IOException {
        
        Path filePath = paystubService.generatePrintablePaystub(id);
        
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}