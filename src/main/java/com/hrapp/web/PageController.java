package com.hrapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.hrapp.domain.User;
import com.hrapp.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        User user = securityUser.getUser();

        model.addAttribute("user", user);
        return "home";
    }


    @GetMapping("/homeRH")
    public String homeRH(Authentication authentication, Model model) {
        
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = securityUser.getUser();
        
        model.addAttribute("user", user);
        return "homeRH"; 
    }

    
    @GetMapping("/homeDRH")
    public String homeDRH(Authentication authentication, Model model) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = securityUser.getUser();
        
        model.addAttribute("user", user);
        return "homeDRH"; 
    }

    
    @GetMapping("/homeADMIN")
    public String homeADMIN(Authentication authentication, Model model) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = securityUser.getUser();
        
        model.addAttribute("user", user);
        return "homeADMIN";
    }

    @GetMapping("/logout")
    public String logout(){
        return "login";
    }

    @GetMapping("/employee")
    public String employee(Authentication authentication, Model model) {
    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    User user = securityUser.getUser();
    model.addAttribute("user", user);
    return "employee";
}

}
