package SistemaClasificacionVentas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase principal encargada de procesar, leer y consolidar la información
 * de los archivos planos generados por GenerateInfoFiles.
 */
public class main {

    public static void main(String[] args) {
        System.out.println("Iniciando la ejecución del motor de procesamiento de ventas...");

        try {
            // 1. Validar la existencia de los archivos maestros en la raíz del proyecto
            File archivoProductos = new File("productos.txt");
            File archivoVendedores = new File("vendedores.txt");

            if (!archivoProductos.exists() || !archivoVendedores.exists()) {
                System.out.println("ERROR: No se encontraron los archivos maestros en la raíz del proyecto.");
                System.out.println("Asegúrese de ejecutar primero la clase GenerateInfoFiles.");
                return;
            }

            // 2. Simulación de lectura preliminar (Estructura base para la Entrega 2)
            System.out.println("-> Verificando archivo de productos: [OK]");
            leerArchivoPrueba("productos.txt");

            System.out.println("-> Verificando archivo de vendedores: [OK]");
            leerArchivoPrueba("vendedores.txt");

            // TODO: Implementar la lectura masiva de archivos de ventas individuales,
            // el cálculo de recaudos por empleado y la exportación de los archivos CSV.

            System.out.println("\nEJECUCIÓN PRELIMINAR FINALIZADA CON ÉXITO.");

        } catch (Exception e) {
            System.out.println("ERROR CRÍTICO DURANTE EL PROCESAMIENTO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar de prueba para validar la lectura línea por línea con BufferedReader.
     * @param rutaArchivo Nombre del archivo plano a leer.
     */
    private static void leerArchivoPrueba(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int contadorLineas = 0;
            // Lee el archivo línea por línea hasta el final
            while ((linea = br.readLine()) != null && contadorLineas < 3) {
                System.out.println("   [Lectura Muestra] " + linea);
                contadorLineas++;
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo: " + rutaArchivo);
        }
    }
}