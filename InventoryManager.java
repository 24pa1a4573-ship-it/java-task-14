import java.io.*;
import java.util.*;

public class InventoryManager {

    private static final String FILE_NAME = "inventory.dat";
    private static HashMap<Integer, Product> inventory = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadInventory();

        while (true) {
            try {
                showMenu();
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> addProduct();
                    case 2 -> updateProduct();
                    case 3 -> deleteProduct();
                    case 4 -> viewProducts();
                    case 5 -> inventorySummary();
                    case 6 -> {
                        saveInventory();
                        System.out.println("Exiting application...");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    // ---------------- MENU ----------------
    private static void showMenu() {
        System.out.println("\n=== Inventory Management System ===");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. View Products");
        System.out.println("5. Inventory Summary");
        System.out.println("6. Exit");
        System.out.print("Enter choice: ");
    }

    // ---------------- ADD ----------------
    private static void addProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(sc.nextLine());

            if (inventory.containsKey(id)) {
                System.out.println("Product ID already exists.");
                return;
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(sc.nextLine());

            System.out.print("Enter Price: ");
            double price = Double.parseDouble(sc.nextLine());

            inventory.put(id, new Product(id, name, qty, price));
            saveInventory();
            System.out.println("Product added successfully.");

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    // ---------------- UPDATE ----------------
    private static void updateProduct() {
        System.out.print("Enter Product ID to update: ");
        int id = Integer.parseInt(sc.nextLine());

        Product p = inventory.get(id);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("New Name: ");
        p.setName(sc.nextLine());

        System.out.print("New Quantity: ");
        p.setQuantity(Integer.parseInt(sc.nextLine()));

        System.out.print("New Price: ");
        p.setPrice(Double.parseDouble(sc.nextLine()));

        saveInventory();
        System.out.println("Product updated.");
    }

    // ---------------- DELETE ----------------
    private static void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        if (inventory.remove(id) != null) {
            saveInventory();
            System.out.println("Product deleted.");
        } else {
            System.out.println("Product not found.");
        }
    }

    // ---------------- VIEW ----------------
    private static void viewProducts() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory empty.");
            return;
        }
        inventory.values().forEach(System.out::println);
    }

    // ---------------- SUMMARY ----------------
    private static void inventorySummary() {
        int totalItems = 0;
        double totalValue = 0;

        for (Product p : inventory.values()) {
            totalItems += p.getQuantity();
            totalValue += p.getQuantity() * p.getPrice();
        }

        System.out.println("Total Products: " + inventory.size());
        System.out.println("Total Items: " + totalItems);
        System.out.println("Total Inventory Value: " + totalValue);
    }

    // ---------------- FILE HANDLING ----------------
    private static void saveInventory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadInventory() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_NAME))) {
            inventory = (HashMap<Integer, Product>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error loading inventory.");
        }
    }
}