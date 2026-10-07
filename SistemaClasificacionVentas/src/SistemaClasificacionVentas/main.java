package SistemaClasificacionVentas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase principal encargada de procesar masivamente los archivos de ventas,
 * productos y vendedores, consolidar la información y generar los reportes CSV.
 */
public class main {

    // Estructuras auxiliares para almacenar información general
    private static Map<String, String> productosNombres = new HashMap<>();
    private static Map<String, Double> productosPrecios = new HashMap<>();
    private static Map<String, String> vendedoresNombres = new HashMap<>();

    // Estructuras para acumular totales de procesamiento
    private static Map<String, Double> ventasPorVendedor = new HashMap<>();
    private static Map<String, Long> cantidadPorProducto = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Iniciando procesamiento de datos para la Entrega Final...");

        try {
            // 1. Cargar datos base
            cargarProductos();
            cargarVendedores();

            // 2. Procesar todos los archivos de ventas en el directorio actual
            procesarArchivosVentas();

            // 3. Generar reportes CSV
            generarReporteVendedores();
            generarReporteProductos();

            System.out.println("PROCESAMIENTO Y GENERACIÓN DE REPORTES FINALIZADOS CON ÉXITO.");

        } catch (Exception e) {
            System.err.println("Ocurrió un error crítico durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lee el archivo de productos y carga sus nombres y precios.
     */
    private static void cargarProductos() throws IOException {
        File file = new File("productos.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String nombre = parts[1].trim();
                    double precio = Double.parseDouble(parts[2].trim().replace(",", "."));

                    // Validación de datos erróneos (Punto Extra)
                    if (precio >= 0) {
                        productosNombres.put(id, nombre);
                        productosPrecios.put(id, precio);
                        cantidadPorProducto.put(id, 0L);
                    }
                }
            }
        }
    }

    /**
     * Lee el archivo de vendedores y mapea documento con nombre completo.
     */
    private static void cargarVendedores() throws IOException {
        File file = new File("vendedores.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String doc = parts[1].trim();
                    String nombreCompleto = parts[2].trim() + " " + parts[3].trim();

                    vendedoresNombres.put(doc, nombreCompleto);
                    ventasPorVendedor.put(doc, 0.0);
                }
            }
        }
    }

    /**
     * Escanea el directorio del proyecto y procesa cada archivo de ventas encontrado.
     */
    private static void procesarArchivosVentas() throws IOException {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("ventas_") && name.endsWith(".txt"));

        if (files == null) return;

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String primeraLinea = reader.readLine();
                if (primeraLinea == null) continue;

                String[] cabecera = primeraLinea.split(";");
                if (cabecera.length < 2) continue;

                String docVendedor = cabecera[1].trim();
                double totalVentaArchivo = 0.0;

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 2) {
                        String idProducto = parts[0].trim();
                        long cantidad = Long.parseLong(parts[1].trim());

                        // Validación de cantidad y producto existente (Punto Extra)
                        if (cantidad > 0 && productosPrecios.containsKey(idProducto)) {
                            double precioUnitario = productosPrecios.get(idProducto);
                            totalVentaArchivo += (precioUnitario * cantidad);

                            // Acumular cantidad por producto
                            cantidadPorProducto.put(idProducto, cantidadPorProducto.getOrDefault(idProducto, 0L) + cantidad);
                        }
                    }
                }

                // Acumular total vendido al vendedor
                ventasPorVendedor.put(docVendedor, ventasPorVendedor.getOrDefault(docVendedor, 0.0) + totalVentaArchivo);
            }
        }
    }

    /**
     * Genera el archivo CSV de vendedores ordenado de mayor a menor recaudación.
     */
    private static void generarReporteVendedores() throws IOException {
        List<Map.Entry<String, Double>> lista = new ArrayList<>(ventasPorVendedor.entrySet());

        // Ordenar descendentemente por dinero
        lista.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_vendedores.csv"))) {
            for (Map.Entry<String, Double> entry : lista) {
                String doc = entry.getKey();
                String nombre = vendedoresNombres.getOrDefault(doc, "Vendedor Desconocido");
                double total = entry.getValue();

                writer.write(nombre + ";" + String.format("%.2f", total));
                writer.newLine();
            }
        }
    }

    /**
     * Genera el archivo CSV de productos ordenado de mayor a menor cantidad vendida.
     */
    private static void generarReporteProductos() throws IOException {
        List<Map.Entry<String, Long>> lista = new ArrayList<>(cantidadPorProducto.entrySet());

        // Ordenar descendentemente por cantidad
        lista.sort((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_productos.csv"))) {
            for (Map.Entry<String, Long> entry : lista) {
                String id = entry.getKey();
                String nombre = productosNombres.getOrDefault(id, "Producto Desconocido");
                double precio = productosPrecios.getOrDefault(id, 0.0);

                writer.write(nombre + ";" + String.format("%.2f", precio));
                writer.newLine();
            }
        }
    }
}