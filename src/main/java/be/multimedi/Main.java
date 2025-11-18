package be.multimedi;

import be.multimedi.model.EPC;
import be.multimedi.model.Property;
import be.multimedi.model.PropertyType;
import be.multimedi.service.PropertyService;
import be.multimedi.service.PropertyServiceImpl;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        PropertyService service = new PropertyServiceImpl();

        try {
            runMenu(service);
        } finally {
            service.close();
            System.out.println("Goodbye!");
        }
    }

    private static void runMenu(PropertyService service) {
        while (true) {
            System.out.println("\n===== Real Estate Application =====");
            System.out.println("1. Register a new property");
            System.out.println("2. Show all properties");
            System.out.println("3. Search properties by address");
            System.out.println("4. Select property and view/delete");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    Property p = createPropertyFromUserInput();
                    service.registerProperty(p);
                    System.out.println("Property saved!");
                    break;

                case "2":
                    showAllProperties(service);
                    break;

                case "3":
                    askUserToSearchByAddress(service);
                    break;

                case "4":
                    selectPropertyFromListAndMaybeDelete(service);
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // ---------- CREATE PROPERTY ----------

    private static Property createPropertyFromUserInput() {
        Property property = new Property();

        System.out.print("Enter address: ");
        property.setAddress(scanner.nextLine());

        System.out.print("Enter selling price: ");
        property.setPrice(Double.parseDouble(scanner.nextLine()));

        System.out.println("Enter property type " +
                Arrays.toString(PropertyType.values()) + " :");
        property.setType(readPropertyType());

        System.out.println("Enter EPC " +
                Arrays.toString(EPC.values()) + " :");
        property.setEpc(readEpc());

        System.out.print("Enter living area (interior m²): ");
        property.setInteriorArea(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter plot area (m²): ");
        property.setPlotArea(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter number of bedrooms: ");
        property.setNrBedrooms(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter number of bathrooms: ");
        property.setNrBathrooms(Integer.parseInt(scanner.nextLine()));

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
            String input = scanner.nextLine()
                    .trim()
                    .toUpperCase()
                    .replace("+", "_PLUS");

            try {
                return EPC.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.print("Invalid EPC. Please enter one of " +
                        Arrays.toString(EPC.values()) + " : ");
            }
        }
    }

    // ---------- DISPLAY / SEARCH ----------

    private static void showAllProperties(PropertyService service) {
        System.out.println("\n--- All Properties ---");
        List<Property> properties = service.getAllProperties();

        if (properties.isEmpty()) {
            System.out.println("No properties found.");
        } else {
            properties.forEach(System.out::println);
        }
    }

    private static void askUserToSearchByAddress(PropertyService service) {
        System.out.print("\nEnter search term for address: ");
        String searchTerm = scanner.nextLine();

        List<Property> results = service.searchByAddress(searchTerm);

        if (results.isEmpty()) {
            System.out.println("No properties found for: " + searchTerm);
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private static void selectPropertyFromListAndMaybeDelete(PropertyService service) {
        List<Property> properties = service.getAllProperties();

        if (properties.isEmpty()) {
            System.out.println("No properties to select.");
            return;
        }

        System.out.println("\n--- Select a Property ---");
        for (int i = 0; i < properties.size(); i++) {
            Property p = properties.get(i);
            System.out.printf("%d) [id=%d] %s (%.2f €)%n",
                    i + 1, p.getId(), p.getAddress(), p.getPrice());
        }

        System.out.print("Enter the id number of the property to view (or 0 to cancel): ");
        String input = scanner.nextLine();

        int index;
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }

        if (index == 0) {
            System.out.println("Cancelled.");
            return;
        }

        if (index < 1 || index > properties.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Property selected = properties.get(index - 1);

        System.out.println("\n--- Property Details ---");
        System.out.println(selected);

        System.out.print("Do you want to delete this property? (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("y") || answer.equals("yes")) {
            service.deleteProperty(selected);
            System.out.println("Property deleted.");

            System.out.println("\nUpdated list of properties:");
            showAllProperties(service);
        } else {
            System.out.println("Property was not deleted.");
        }
    }
}