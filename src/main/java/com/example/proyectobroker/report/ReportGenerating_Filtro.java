package com.example.proyectobroker.report;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import database.ConnectMysql;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clase que genera un report con la libreria iText, para obtener
 * los datos de un usuario concreto en la tabla USER
 *
 */
public class ReportGenerating_Filtro {
    public void generateReport(Connection conn, String nombre) {
        try {
            //nos aseguramos de que exista el fichero o si no lo creamos
            File reportsFile = new File("REPORTS");
            if (!reportsFile.exists()) {
                reportsFile.mkdir();
            }
            PdfWriter writer = new PdfWriter(new FileOutputStream("REPORTS/reporte_" + nombre + ".pdf"));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Paragraph title = new Paragraph("REPORTE DEL USUARIO (username = '" + nombre + "')")
                    .setFontSize(16);
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));

            table.addCell(new Cell().add(new Paragraph("USERNAME")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("PASSWORD")).setTextAlignment(TextAlignment.CENTER));

            // Consulta SQL filtrada: tabla 'users', columnas 'username' y 'password'
            Statement stmt = conn.createStatement();
            String sql = "SELECT username, password FROM users WHERE username = '" + nombre + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                table.addCell(new Cell().add(new Paragraph(rs.getString("username"))).setTextAlignment(TextAlignment.LEFT));
                table.addCell(new Cell().add(new Paragraph(rs.getString("password"))).setTextAlignment(TextAlignment.LEFT));
            }

            document.add(table);
            document.close();
            System.out.println("Reporte generado exitosamente para " + nombre);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para establecer la conexión con la base de datos SQLite
    public static Connection connect() {
        return new ConnectMysql().conectar();
    }

    public static void main(String[] args) {
        // Conectar a la base de datos y generar el reporte para un nombre específico
        String nombre = "emmanuel";  // Cambia el valor aquí para filtrar por diferentes nombres
        Connection conn = connect();
        if (conn != null) {
            ReportGenerating_Filtro generator = new ReportGenerating_Filtro();
            generator.generateReport(conn, nombre);
        } else {
            System.out.println("Error al conectar a la base de datos.");
        }
    }
}