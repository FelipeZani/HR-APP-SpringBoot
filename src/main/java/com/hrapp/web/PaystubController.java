package com.hrapp.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hrapp.domain.Paystub;
import com.hrapp.service.PaystubService;

@RestController
@RequestMapping("/api/paystubs")
@CrossOrigin
public class PaystubController {
    private final PaystubService service;

    public PaystubController(PaystubService service) { this.service = service; }

    @GetMapping public List<Paystub> list() { return service.list(); }
    @GetMapping("/{id}") public Paystub get(@PathVariable Long id) { return service.get(id); }
    @PostMapping public Paystub create(@RequestBody Paystub p) { return service.create(p); }
    
}
