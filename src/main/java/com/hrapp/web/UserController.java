// ProjetJEESpringBoot/src/main/java/com/hrapp/web/UserController.java (NOUVEAU)

package com.hrapp.web;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize; // Nécessaire pour la sécurité
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hrapp.domain.User;
import com.hrapp.service.UserService;
import com.hrapp.web.dto.UserCreationForm;

@Controller
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String listAdminUsers(Model model) {
        List<User> users = userService.getRHAndDRHUsers();
        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "Gestion des Utilisateurs RH et DRH");
        return "users/list"; 
    }

    
    @PreAuthorize("hasRole('DRH')")
    @GetMapping("/drh")
    public String listDRHUsers(Model model) {
        List<User> users = userService.getRHUsers();
        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "Gestion des Utilisateurs RH");
        return "users/list"; 
    }

    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("successMessage", "Utilisateur supprimé avec succès.");
        return "redirect:/users/admin"; 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("userCreationForm", new UserCreationForm()); 
        model.addAttribute("availableRoles", List.of("RH", "DRH")); 
        return "users/form"; 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userCreationForm") UserCreationForm form, RedirectAttributes ra) { 
        
        if (form.getUsername() == null || form.getUsername().isEmpty() || form.getEmail() == null || form.getEmail().isEmpty()) {
             ra.addFlashAttribute("errorMessage", "Le nom d'utilisateur et l'email sont obligatoires.");
             return "redirect:/users/new";
        }
        
        User newUser = new User();
        newUser.setUsername(form.getUsername());
        newUser.setRole(form.getRole());

        String rawPassword = UserService.generateRandomPassword();
        
        userService.registerNewUser(newUser, rawPassword, form.getEmail());
        
        ra.addFlashAttribute("successMessage", "Compte '" + form.getUsername() + "' créé et identifiants envoyés à " + form.getEmail() + ".");
        return "redirect:/users/admin"; 
    }
}
