package com.hrapp.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hrapp.service.EmployeeService;
import com.hrapp.service.ProjectService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.AreaBreak;

import com.hrapp.utils.ChartGenerator;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final EmployeeService employeeService;
    private final ProjectService projectService;

    public ReportController(EmployeeService employeeService, ProjectService projectService) {
        this.employeeService = employeeService;
        this.projectService = projectService;
    }
    
    @GetMapping("/consolidated-report")
    public ResponseEntity<Resource> generateConsolidatedReport() throws IOException {
        
        // 1. Récupération des données
        Map<String, Long> employeesByDept = employeeService.getEmployeeCountByDepartement();
        Map<String, Long> projectsByStatus = projectService.getProjectCountByStatus();
        
        // 2. Génération des chemins pour les images
        Path reportPath = generatePdfReport(employeesByDept, projectsByStatus);

        // 3. Servir le PDF généré
        Resource resource = new UrlResource(reportPath.toUri());

        if (resource.exists()) {
            MediaType mediaType = MediaType.APPLICATION_PDF;
            
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"") 
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private Path generatePdfReport(Map<String, Long> employeesByDept, Map<String, Long> projectsByStatus) throws IOException {
            String fileName = "HR_Consolidated_Report.pdf";
            Path outputPath = Path.of(System.getProperty("java.io.tmpdir"), fileName);
            
            Path deptChartPath = null;
            Path projectChartPath = null;
            
            try {
                // --- 1. Générer l'Image du Camembert des Départements ---
                deptChartPath = ChartGenerator.createPieChart(
                    employeesByDept, 
                    "Répartition des Employés par Département", 
                    "dept_chart.png"
                );

                // --- 2. Générer l'Image du Camembert des Projets ---
                projectChartPath = ChartGenerator.createPieChart(
                    projectsByStatus, 
                    "Avancement des Projets", 
                    "project_status_chart.png"
                );
                
                // 3. Création du PDF avec iText
                try (PdfWriter writer = new PdfWriter(outputPath.toString());
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf)) {

                    document.add(new Paragraph("RAPPORT CONSOLIDÉ RH")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(24).setBold());
                    document.add(new Paragraph("Généré le : " + new Date()).setTextAlignment(TextAlignment.CENTER));
                    document.add(new Paragraph("\n"));
                    
                    // ----------------------------------------------------
                    // 1. INTÉGRATION DU GRAPHIQUE DES EMPLOYES
                    // ----------------------------------------------------
                    document.add(new Paragraph("1. Répartition des Employés par Département")
                            .setFontSize(16).setBold());
                    
                    // Intégration de l'image du graphique dans le PDF
                    Image deptChart = new Image(ImageDataFactory.create(deptChartPath.toAbsolutePath().toString()));
                    document.add(deptChart);

                    document.add(new AreaBreak());

                    // ----------------------------------------------------
                    // 2. INTÉGRATION DU GRAPHIQUE DES PROJETS
                    // ----------------------------------------------------
                    document.add(new Paragraph("2. Répartition des Projets par Statut")
                            .setFontSize(16).setBold());
                    
                    // Intégration de l'image du graphique dans le PDF
                    Image projectChart = new Image(ImageDataFactory.create(projectChartPath.toAbsolutePath().toString()));
                    document.add(projectChart);
                }

            } catch (Exception e) {
                System.err.println("Erreur lors de la génération du rapport PDF: " + e.getMessage());
                throw new IOException("Erreur interne lors de la création du rapport ou du graphique.", e);
            } finally {
                // NETTOYAGE : Supprimez les fichiers images temporaires après utilisation
                if (deptChartPath != null) Files.deleteIfExists(deptChartPath);
                if (projectChartPath != null) Files.deleteIfExists(projectChartPath);
            }

            return outputPath;
        }
}
