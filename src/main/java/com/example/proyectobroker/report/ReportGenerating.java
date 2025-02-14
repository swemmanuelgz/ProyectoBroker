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

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clase que genera un report con la libreria iText, para obtener
 * un listado de usuarios en la tabla USER
 *
 */
public class ReportGenerating {
    public void generateFullReport(String dest) {
        try (Connection conn = new ConnectMysql().conectar()) {
            if (conn != null) {
                PdfWriter writer = new PdfWriter(new FileOutputStream(dest));
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                Paragraph title = new Paragraph("REPORTE DE TODOS LOS USUARIOS DE NUESTRA APLICACIÓN")
                        .setFontSize(16)
                        .setTextAlignment(TextAlignment.CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));

                Table table = new Table(2);
                table.setWidth(UnitValue.createPercentValue(100));
                table.addCell(new Cell().add(new Paragraph("USERNAME")).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph("PASSWORD")).setTextAlignment(TextAlignment.CENTER));

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT username, password FROM users");

                while (rs.next()) {
                    table.addCell(new Cell().add(new Paragraph(rs.getString("username"))).setTextAlignment(TextAlignment.LEFT));
                    table.addCell(new Cell().add(new Paragraph(rs.getString("password"))).setTextAlignment(TextAlignment.LEFT));
                }

                document.add(table);
                document.close();
                System.out.println("Reporte generado exitosamente.");
            } else {
                System.out.println("Error al conectar a la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void generateReport(Connection conn) {
        try {
            // Crea un documento PDF y un PdfDocument asociado

            PdfWriter writer = new PdfWriter(new FileOutputStream("REPORTS/reporte_users.pdf"));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título del reporte
            Paragraph title = new Paragraph("REPORTE DE TODOS LOS USUARIOS DE NUESTRA APLICACIÓN")
                    .setFontSize(16);
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));  // Salto de línea

            // Crear la tabla con 2 columnas
            Table table = new Table(2); // 2 columnas

            // Establecer los anchos de las columnas en porcentaje
            table.setWidth(UnitValue.createPercentValue(100)); // Hace que la tabla ocupe el 100% del ancho disponible

            // Agregar los encabezados de la tabla
            table.addCell(new Cell().add(new Paragraph(" username")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph("password")).setTextAlignment(TextAlignment.CENTER));

            // Obtener los datos de la base de datos
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username, password FROM users");

            // Agregar los datos de la base de datos a la tabla
            while (rs.next()) {
                table.addCell(new Cell().add(new Paragraph(rs.getString("username"))).setTextAlignment(TextAlignment.LEFT));
                table.addCell(new Cell().add(new Paragraph(rs.getString("password"))).setTextAlignment(TextAlignment.LEFT));
            }

            // Añadir la tabla al documento PDF
            document.add(table);

            // Cerrar el documento
            document.close();

            System.out.println("Reporte generado exitosamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // Método para establecer la conexión con la base de datos SQLite
    public static Connection connect() {
        return new ConnectMysql().conectar();
    }

    public static void main(String[] args) {
        // Conectar a la base de datos y generar el reporte
        Connection conn = connect();
        if (conn != null) {
            ReportGenerating generator = new ReportGenerating();
            generator.generateReport(conn);
        } else {
            System.out.println("Error al conectar a la base de datos.");
        }
    }
}
