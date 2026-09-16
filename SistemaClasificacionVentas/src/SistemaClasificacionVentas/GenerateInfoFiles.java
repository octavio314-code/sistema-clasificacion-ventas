package SistemaClasificacionVentas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {

    private static final Random random = new Random();
    private static final String[] NOMBRES = {"Carlos", "Maria", "Juan", "Ana", "Luis", "Diana"};
    private static final String[] APELLIDOS = {"Gomez", "Perez", "Rodriguez", "Lopez", "Martinez"};
    private static final String[] PRODUCTOS = {"Motor Trifasico", "Compresor", "Valvula", "Sensor Inductivo", "Rele"};
    private static final String[] TIPOS_DOC = {"CC", "CE", "NIT"};

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando la generacion de datos de prueba...");

            createProductsFile(10);
            createSalesManInfoFile(5);
            createSalesMenFile(15, "Carlos Gomez", 100123456L);

            System.out.println("EJECUCION EXITOSA: Todos los archivos fueron generados correctamente.");
        } catch (Exception e) {
            System.out.println("ERROR DURANTE LA EJECUCION: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        String fileName = "ventas_" + id + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)))) {
            writer.write(TIPOS_DOC[random.nextInt(TIPOS_DOC.length)] + ";" + id + "\n");
            for (int i = 0; i < randomSalesCount; i++) {
                String idProducto = "PROD-" + (random.nextInt(10) + 1);
                int cantidad = random.nextInt(20) + 1; 
                writer.write(idProducto + ";" + cantidad + ";\n");
            }
        }
    }

    public static void createProductsFile(int productsCount) throws IOException {
        String fileName = "productos.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)))) {
            for (int i = 1; i <= productsCount; i++) {
                String idProducto = "PROD-" + i;
                String nombreProducto = PRODUCTOS[random.nextInt(PRODUCTOS.length)] + " " + i;
                double precio = 50000 + (250000 - 50000) * random.nextDouble(); 
                writer.write(String.format("%s;%s;%.2f\n", idProducto, nombreProducto, precio));
            }
        }
    }

    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        String fileName = "vendedores.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)))) {
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDoc = TIPOS_DOC[random.nextInt(TIPOS_DOC.length)];
                long numDoc = 100000000L + random.nextInt(900000000);
                String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
                writer.write(tipoDoc + ";" + numDoc + ";" + nombre + ";" + apellido + "\n");
            }
        }
    }
}