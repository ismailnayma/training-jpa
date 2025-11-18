package be.multimedi;

import be.multimedi.model.EPC;
import be.multimedi.model.Property;
import be.multimedi.model.PropertyType;
import be.multimedi.repository.PropertyRepository;
import be.multimedi.repository.PropertyRepositoryImpl;
import org.h2.tools.Server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        PropertyRepositoryImpl repo = new PropertyRepositoryImpl();

        try {
            runMenu(repo);
        } finally {
            repo.close();
            System.out.println("Goodbye!");
        }
    }

    private static void runMenu(PropertyRepository repo) {
        while (true) {
            System.out.println("\n===== Real Estate Application =====");
            System.out.println("1. Register a new property");
            System.out.println("2. Show all properties");
            System.out.println("3. Search properties by address");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    Property p = createPropertyFromUserInput();
                    repo.save(p);
                    System.out.println("Property saved!");
                    break;

                case "2":
                    showAllProperties(repo);
                    break;

                case "3":
                    askUserToSearchByAddress(repo);
                    break;

                case "0":
                    return; // exit the method → exit program

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static Property createPropertyFromUserInput() {
        Property property = new Property();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        property.setAddress(address);

        System.out.print("Enter selling price: ");
        double price = Double.parseDouble(scanner.nextLine());
        property.setPrice(price);

        // Property type
        System.out.println("Enter property type " +
                Arrays.toString(PropertyType.values()) + " :");
        PropertyType type = readPropertyType();
        property.setType(type);

        // EPC
        System.out.println("Enter EPC " +
                Arrays.toString(EPC.values()) + " :");
        EPC epc = readEpc();
        property.setEpc(epc);

        System.out.print("Enter living area (interior m²): ");
        int interiorArea = Integer.parseInt(scanner.nextLine());
        property.setInteriorArea(interiorArea);

        System.out.print("Enter plot area (m²): ");
        int plotArea = Integer.parseInt(scanner.nextLine());
        property.setPlotArea(plotArea);

        System.out.print("Enter number of bedrooms: ");
        int nrBedrooms = Integer.parseInt(scanner.nextLine());
        property.setNrBedrooms(nrBedrooms);

        System.out.print("Enter number of bathrooms: ");
        int nrBathrooms = Integer.parseInt(scanner.nextLine());
        property.setNrBathrooms(nrBathrooms);

        return property;
    }

    private static PropertyType readPropertyType() {
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return PropertyType.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.print("Invalid type. Please enter one of " +
                        Arrays.toString(PropertyType.values()) + " : ");
            }
        }
    }

    private static EPC readEpc() {
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase()
                    .replace("+", "_PLUS"); // "A+" -> "A_PLUS"
            try {
                return EPC.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.print("Invalid EPC. Please enter one of " +
                        Arrays.toString(EPC.values()) + " : ");
            }
        }
    }

    private static void showAllProperties(PropertyRepository repo) {
        List<Property> properties = repo.findAll();
        properties.forEach(System.out::println);
    }

    private static void askUserToSearchByAddress(PropertyRepository repo) {
        System.out.print("\nEnter address search term: ");
        String searchTerm = scanner.nextLine();

        List<Property> results = repo.findByAddress(searchTerm);

        if (results.isEmpty()) {
            System.out.println("No properties found for search term: " + searchTerm);
        } else {
            System.out.println("Found properties:");
            results.forEach(System.out::println);
        }
    }
}