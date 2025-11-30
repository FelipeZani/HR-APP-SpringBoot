package com.hrapp.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class ChartGenerator {

    /**
     * Crée un camembert à partir d'une Map de données et l'enregistre au format PNG.
     * @param data La Map avec les catégories et les comptes (ex: "IT", 12L)
     * @param title Le titre du graphique
     * @param fileName Le nom du fichier image (ex: "dept_chart.png")
     * @return Le chemin d'accès au fichier PNG généré
     * @throws IOException
     */
    public static Path createPieChart(Map<String, Long> data, String title, String fileName) throws IOException {
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            
            dataset.setValue(entry.getKey(), entry.getValue().doubleValue());
        }

        
        JFreeChart chart = ChartFactory.createPieChart(
            title,           // Titre
            dataset,         // Données
            true,            // Légende
            true,            // Tooltips
            false            // URL
        );
        
        
        Path outputPath = Path.of(System.getProperty("java.io.tmpdir"), fileName);
        File chartFile = outputPath.toFile();

        
        ChartUtils.saveChartAsPNG(
            chartFile,
            chart,
            500, // Largeur
            300  // Hauteur
        );

        return outputPath;
    }
}
