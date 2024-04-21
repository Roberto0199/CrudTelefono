package org.example;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = database.getCollection("telefonos");

            Scanner scanner = new Scanner(System.in);
            boolean continuar = true;

            while (continuar) {
                System.out.println("Menu:");
                System.out.println("1. Agregar teléfono");
                System.out.println("2. Mostrar todos los teléfonos");
                System.out.println("3. Eliminar teléfono");
                System.out.println("4. Actualizar teléfono");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        agregarTelefono(collection, scanner);
                        break;
                    case 2:
                        mostrarTelefonos(collection);
                        break;
                    case 3:
                        eliminarTelefono(collection, scanner);
                        break;
                    case 4:
                        actualizarTelefono(collection, scanner);
                        break;
                    case 5:
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void agregarTelefono(MongoCollection<Document> collection, Scanner scanner) {
        System.out.print("Ingrese la marca del teléfono: ");
        String marca = scanner.next();
        System.out.print("Ingrese el modelo del teléfono: ");
        String modelo = scanner.next();
        System.out.print("Ingrese el sistema operativo del teléfono: ");
        String sistemaOperativo = scanner.next();
        System.out.print("Ingrese el tamaño de pantalla del teléfono: ");
        double tamanoPantalla = scanner.nextDouble();
        System.out.print("Ingrese la memoria RAM del teléfono: ");
        int memoriaRAM = scanner.nextInt();
        System.out.print("Ingrese el almacenamiento interno del teléfono: ");
        int almacenamientoInterno = scanner.nextInt();
        System.out.print("¿Tiene cámara? (true/false): ");
        boolean tieneCamara = scanner.nextBoolean();
        System.out.print("Ingrese la resolución de la cámara del teléfono: ");
        int resolucionCamara = scanner.nextInt();
        System.out.print("¿Es smartphone? (true/false): ");
        boolean esSmartphone = scanner.nextBoolean();
        System.out.print("Ingrese el IMEI del teléfono: ");
        String imei = scanner.next();

        Document document = new Document("marca", marca)
                .append("modelo", modelo)
                .append("sistemaOperativo", sistemaOperativo)
                .append("tamanoPantalla", tamanoPantalla)
                .append("memoriaRAM", memoriaRAM)
                .append("almacenamientoInterno", almacenamientoInterno)
                .append("tieneCamara", tieneCamara)
                .append("resolucionCamara", resolucionCamara)
                .append("esSmartphone", esSmartphone)
                .append("imei", imei);

        collection.insertOne(document);
        System.out.println("Teléfono agregado correctamente.");
    }

    private static void mostrarTelefonos(MongoCollection<Document> collection) {
        LinkedList<Document> documentList = new LinkedList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                documentList.add(document);
            }
        }

        System.out.println("Documentos recuperados de la base de datos:");
        for (Document document : documentList) {
            System.out.println(document.toJson());
        }
    }

    private static void eliminarTelefono(MongoCollection<Document> collection, Scanner scanner) {
        System.out.print("Ingrese el IMEI del teléfono que desea eliminar: ");
        String imei = scanner.next();

        collection.deleteOne(Filters.eq("imei", imei));
        System.out.println("Teléfono eliminado correctamente.");
    }

    private static void actualizarTelefono(MongoCollection<Document> collection, Scanner scanner) {
        System.out.print("Ingrese el IMEI del teléfono que desea actualizar: ");
        String imei = scanner.next();

        System.out.print("Ingrese el campo que desea actualizar (marca, modelo, sistemaOperativo, tamanoPantalla, memoriaRAM, almacenamientoInterno, tieneCamara, resolucionCamara, esSmartphone): ");
        String campo = scanner.next();

        System.out.print("Ingrese el nuevo valor: ");
        String nuevoValor = scanner.next();

        collection.updateOne(Filters.eq("imei", imei), Updates.set(campo, nuevoValor));
        System.out.println("Teléfono actualizado correctamente.");
    }
}
