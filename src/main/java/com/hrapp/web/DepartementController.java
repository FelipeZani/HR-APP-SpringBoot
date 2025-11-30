package com.hrapp.web;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hrapp.domain.Departement;
import com.hrapp.service.DepartementService;

@RestController
@RequestMapping("/api/departements")
@CrossOrigin
public class DepartementController {
    private final DepartementService service;
    public DepartementController(DepartementService service) { this.service = service; }

    @GetMapping public List<Departement> list() { return service.list(); }
    @PostMapping public Departement create(@RequestBody Departement d) { return service.create(d); }
}
