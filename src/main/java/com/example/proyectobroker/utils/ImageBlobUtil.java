package com.example.proyectobroker.utils;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Clase utilitaria para convertir imágenes a Blob y viceversa.
 */
public class ImageBlobUtil {

    /**
     * Convierte un archivo de imagen en un objeto Blob.
     *
     * @param imageFile El archivo de imagen a convertir.
     * @return Un Blob que contiene los datos de la imagen.
     * @throws IOException  Si ocurre un error al leer el archivo.
     * @throws SQLException Si ocurre un error al crear el Blob.
     */
    public static Blob imageFileToBlob(File imageFile) throws IOException, SQLException {
        // Lee todos los bytes del archivo
        byte[] bytes = Files.readAllBytes(imageFile.toPath());
        // Crea un Blob a partir de los bytes
        return new SerialBlob(bytes);
    }

    /**
     * Convierte un objeto Blob en una imagen (BufferedImage).
     *
     * @param blob El Blob que contiene los datos de la imagen.
     * @return Un BufferedImage creado a partir de los datos del Blob, o null si el Blob es null.
     * @throws SQLException Si ocurre un error al acceder a los datos del Blob.
     * @throws IOException  Si ocurre un error al leer los datos de la imagen.
     */
    public static BufferedImage blobToImage(Blob blob) throws SQLException, IOException {
        if (blob == null) {
            return null;
        }
        // Obtiene los bytes del Blob (nota: en JDBC, el primer byte es la posición 1)
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        // Lee la imagen a partir de los bytes
        return ImageIO.read(bais);
    }
}
