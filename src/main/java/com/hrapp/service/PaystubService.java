package com.hrapp.service;

import java.nio.file.Path;
import java.util.Date;

import com.hrapp.domain.Employee;
import com.hrapp.domain.Paystub;
import com.hrapp.repository.PaystubRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

import com.itextpdf.io.IOException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.text.SimpleDateFormat;


@Service
public class PaystubService {
    private final PaystubRepository repo;
    private final EmployeeService employeeService;

    public PaystubService(PaystubRepository repo, EmployeeService employeeService) { // ATTENTION: METTRE À JOUR LE CONSTRUCTEUR
        this.repo = repo; 
        this.employeeService = employeeService;
    }

    public List<Paystub> list() { return repo.findAll(); }
    public Paystub get(Long id) { return repo.findById(id).orElseThrow(); }

    @Transactional
    public Paystub create(Paystub p) {
        float net = p.getBaseSalary() + p.getBonuses() - p.getDeductions();
        p.setNetTotal(net);
        p.setCreationDate(new Date());
        return repo.save(p); 
    }

    @Transactional
    public List<Paystub> getPaystubsByEmployee(Long employeeId) {
        return repo.findByEmployeeIdOrderByPayPeriodDesc(employeeId);
    }

    @Transactional
    public List<Paystub> searchPaystubs(Long employeeId, Date startDate, Date endDate) {
        if (employeeId != null && startDate != null && endDate != null) {
            return repo.findByEmployeeIdAndPayPeriodBetween(employeeId, startDate, endDate);
        } else if (employeeId != null) {
            return repo.findByEmployeeIdOrderByPayPeriodDesc(employeeId);
        } else if (startDate != null && endDate != null) {
            return repo.findByPayPeriodBetween(startDate, endDate);
        } else {
            return list(); 
        }
    }

@Transactional
    public Path generatePrintablePaystub(Long paystubId) throws IOException, java.io.IOException {
        Paystub paystub = get(paystubId);
        Employee employee = employeeService.get(paystub.getEmployeeId());

        SimpleDateFormat periodFormatter = new SimpleDateFormat("MMMM yyyy");
        String formattedPayPeriod = periodFormatter.format(paystub.getPayPeriod());
        
        
        String fileName = "fiche_paie_" + paystubId + "_" + employee.getMatricule() + ".pdf";
        Path outputPath = Path.of(System.getProperty("java.io.tmpdir"), fileName);
        
        
        try (PdfWriter writer = new PdfWriter(outputPath.toString());
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // --- En-tête ---
            document.add(new Paragraph("FICHE DE PAIE - HR APP")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18));
            document.add(new Paragraph("Période : " + formattedPayPeriod)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));
            document.add(new Paragraph("\n"));

            // --- Infos Employé ---
            document.add(new Paragraph("Employé : " + employee.getFirstName() + " " + employee.getLastName()));
            document.add(new Paragraph("Matricule : " + employee.getMatricule()));
            document.add(new Paragraph("\n"));

            // --- Tableau des Composantes ---
            float[] columnWidths = {200f, 80f, 80f}; // Libellé, Montant, Déduction
            Table table = new Table(columnWidths);

            // En-tête du tableau
            table.addCell(new Paragraph("Libellé").setBold());
            table.addCell(new Paragraph("Gains").setBold().setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Paragraph("Retenues").setBold().setTextAlignment(TextAlignment.RIGHT));

            // Ligne 1: Salaire de Base
            table.addCell(new Paragraph("Salaire de base"));
            table.addCell(new Paragraph(String.format("%.2f €", paystub.getBaseSalary())).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Paragraph("-"));
            
            // Ligne 2: Primes
            table.addCell(new Paragraph("Primes"));
            table.addCell(new Paragraph(String.format("%.2f €", paystub.getBonuses())).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Paragraph("-"));

            // Ligne 3: Déductions
            table.addCell(new Paragraph("Cotisations / Déductions"));
            table.addCell(new Paragraph("-"));
            table.addCell(new Paragraph(String.format("%.2f €", paystub.getDeductions())).setTextAlignment(TextAlignment.RIGHT));
            
            document.add(table);

            // --- Total Net ---
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("NET À PAYER : " + String.format("%.2f €", paystub.getNetTotal()))
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.RIGHT));

        } catch (IOException e) {
            
            System.err.println("Erreur lors de la génération du PDF: " + e.getMessage());
            throw e; 
        }

        
        return outputPath;
    }
    
}
