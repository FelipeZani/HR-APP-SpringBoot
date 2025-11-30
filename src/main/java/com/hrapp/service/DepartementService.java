package com.hrapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hrapp.domain.Departement;
import com.hrapp.repository.DepartementRepository;

@Service
public class DepartementService{
    private final DepartementRepository repo;
    public DepartementService(DepartementRepository repo) { this.repo = repo; }

    public List<Departement> list() { return repo.findAll(); }
    public Departement get(Long id) { return repo.findById(id).orElseThrow(); }
    public Departement create(Departement d) { return repo.save(d); }

}